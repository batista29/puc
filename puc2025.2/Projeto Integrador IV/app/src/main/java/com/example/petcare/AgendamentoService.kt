package com.example.petcare

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.petcare.model.Consulta
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class AgendamentoService(private val db: FirebaseFirestore, private val context: Context) {

    /**
     * Regras:
     * 1) Não permitir agendamento em horário passado
     * 2) Não permitir duas consultas no mesmo horário
     * 3) Necessário intervalo mínimo de 1 hora entre consultas
     * 4) CONSULTAS PENDENTES TAMBÉM BLOQUEIAM horário
     */
    suspend fun criarConsulta(consulta: Consulta): Boolean {
        return try {

            val id = db.collection("consultas").document().id
            val consultaComId = consulta.copy(id = id, createdAt = Timestamp.now())

            // salva na coleção global
            db.collection("consultas")
                .document(id)
                .set(consultaComId)
                .await()

            // salva na subcoleção do tutor
            db.collection("usuarios")
                .document(consulta.petOwnerId)
                .collection("agendamentos")
                .document(id)
                .set(consultaComId)
                .await()

            Toast.makeText(context, "Consulta criada com sucesso", Toast.LENGTH_SHORT).show()
            true

        } catch (e: Exception) {
            Log.e("AgendamentoService", "Erro criar consulta", e)
            Toast.makeText(context, "Erro ao criar consulta: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }

    // ---------------- VALIDAÇÃO DE HORÁRIO PASSADO ----------------

    fun horarioEhPassado(data: String, hora: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val parsed = sdf.parse("$data $hora") ?: return false
            parsed.before(Calendar.getInstance().time)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun validarHorario(
        veterinarioId: String,
        data: String,
        hora: String,
        consultaIdAtual: String? = null
    ): Boolean {

        return try {
            val q = db.collection("consultas")
                .whereEqualTo("veterinarioId", veterinarioId)
                .whereEqualTo("data", data)
                .get()
                .await()

            fun horaParaMinutos(horaStr: String): Int {
                val (h, m) = horaStr.split(":").map { it.toIntOrNull() ?: 0 }
                return h * 60 + m
            }

            val novaHoraMin = horaParaMinutos(hora)

            for (doc in q.documents) {

                // 🟡 Ignorar a própria consulta na edição
                if (consultaIdAtual != null && doc.id == consultaIdAtual) continue

                val existente = doc.toObject(Consulta::class.java) ?: continue

                if (existente.hora.isNullOrBlank()) continue

                val status = existente.status ?: "Pendente"

                // Estes NÃO ocupam horário
                if (status == "Cancelada" || status == "Finalizada") continue

                val existenteHoraMin = horaParaMinutos(existente.hora)
                val diff = abs(novaHoraMin - existenteHoraMin)

                // MESMO HORÁRIO
                if (diff == 0) return false

                // INTERVALO MENOR QUE 1 HORA
                if (diff < 60) return false
            }

            true

        } catch (e: Exception) {
            Log.e("AgendamentoService", "Erro validarHorario", e)
            Toast.makeText(context, "Erro ao validar horário: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }

    // ---------------- VALIDAÇÃO DE CONFLITO DE HORÁRIO ----------------

    suspend fun validarHorario(veterinarioId: String, data: String, hora: String): Boolean {
        return try {

            val consultasDia = db.collection("consultas")
                .whereEqualTo("veterinarioId", veterinarioId)
                .whereEqualTo("data", data)
                .get()
                .await()

            fun horaParaMinutos(str: String): Int {
                val (h, m) = str.split(":").map { it.toIntOrNull() ?: 0 }
                return h * 60 + m
            }

            val novaHoraMinutos = horaParaMinutos(hora)

            for (doc in consultasDia.documents) {

                val existente = doc.toObject(Consulta::class.java) ?: continue
                val existenteHora = existente.hora ?: continue

                val status = existente.status ?: "Pendente"

                // estados que NÃO bloqueiam horário
                if (status == "Cancelada" || status == "Finalizada") continue

                val existenteMin = horaParaMinutos(existenteHora)
                val diferenca = abs(novaHoraMinutos - existenteMin)

                // mesmo horário
                if (diferenca == 0) return false

                // intervalo menor que 1h
                if (diferenca < 60) return false
            }

            true

        } catch (e: Exception) {
            Log.e("AgendamentoService", "Erro validarHorario", e)
            Toast.makeText(context, "Erro ao validar horário: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }

    // ---------------- ATUALIZAR CONSULTA ----------------

    suspend fun atualizarConsulta(consulta: Consulta): Boolean {
        return try {

            // Atualiza consulta global
            db.collection("consultas")
                .document(consulta.id)
                .set(consulta)
                .await()

            // Atualiza consulta do tutor
            if (consulta.petOwnerId.isNotBlank()) {
                db.collection("usuarios")
                    .document(consulta.petOwnerId)
                    .collection("agendamentos")
                    .document(consulta.id)
                    .set(consulta)
                    .await()
            }

            Toast.makeText(context, "Consulta atualizada com sucesso", Toast.LENGTH_SHORT).show()
            true

        } catch (e: Exception) {
            Log.e("AgendamentoService", "Erro atualizar consulta", e)
            Toast.makeText(context, "Erro ao atualizar consulta: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }

    // ---------------- DELETAR CONSULTA ----------------

    suspend fun deletarConsulta(consulta: Consulta): Boolean {
        return try {

            // Deleta da coleção global
            db.collection("consultas")
                .document(consulta.id)
                .delete()
                .await()

            // Deleta da subcoleção do tutor
            if (consulta.petOwnerId.isNotBlank()) {
                db.collection("usuarios")
                    .document(consulta.petOwnerId)
                    .collection("agendamentos")
                    .document(consulta.id)
                    .delete()
                    .await()
            }

            Toast.makeText(context, "Consulta excluída com sucesso", Toast.LENGTH_SHORT).show()
            true

        } catch (e: Exception) {
            Log.e("AgendamentoService", "Erro deletar consulta", e)
            Toast.makeText(context, "Erro ao excluir consulta: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }
}
