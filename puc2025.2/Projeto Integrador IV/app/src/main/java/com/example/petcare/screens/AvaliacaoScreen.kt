package com.example.petcare.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import com.example.petcare.ui.theme.PetCareTheme // Assumindo que o tema continua aqui
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --------------------------------------------------------
// MODELO DE DADOS AUXILIAR (AVALIACAO)
// --------------------------------------------------------


// --------------------------------------------------------
// CORES UTILIZADAS NAS OUTRAS TELAS (DEFINIDAS AQUI PARA CONSISTÊNCIA)
// --------------------------------------------------------
private val PrimaryColor = Color(0xFF2A5C67) // Azul PetCare (Título, Destaque)
private val SuccessColor = Color(0xFF4CAF50)  // Verde de Ação/Sucesso (Botões)

// --------------------------------------------------------
// FUNÇÃO PARA RECALCULAR A MÉDIA (Não Mudou, Mantida por Completude)
// --------------------------------------------------------
private suspend fun recalculateVetRating(db: FirebaseFirestore, vetId: String) {
    try {
        val avaliacoesSnapshot = db.collection("avaliacoes")
            .whereEqualTo("veterinarioId", vetId)
            .get()
            .await()

        val avaliacoes = avaliacoesSnapshot.documents.mapNotNull { it.toObject(Avaliacao::class.java) }

        if (avaliacoes.isNotEmpty()) {
            val media = avaliacoes.map {
                it.nota.toDoubleOrNull() ?: 0.0
            }.average()

            db.collection("usuarios").document(vetId)
                .update("nota", media)
                .await()
        }
    } catch (e: Exception) {
        Log.e("AvaliacaoScreen", "Erro ao recalcular média: ${e.message}")
    }
}

// --------------------------------------------------------
// ACTIVITY
// --------------------------------------------------------
class AvaliacaoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vetId = intent.getStringExtra("vetId") ?: ""
        val consultaId = intent.getStringExtra("consultaId") ?: ""

        setContent {
            PetCareTheme {
                AvaliarVeterinarioScreen(
                    vetId = vetId,
                    consultaId = consultaId,
                    onSuccess = {
                        Toast.makeText(this, "Avaliação enviada com sucesso!", Toast.LENGTH_LONG).show()
                        finish()
                    },
                    onError = {
                        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }
}

// --------------------------------------------------------
// COMPOSABLE
// --------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliarVeterinarioScreen(
    vetId: String,
    consultaId: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tutorUid = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        containerColor = Color.White // Fundo branco explícito
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Avaliar Veterinário",
                fontSize = 28.sp,
                color = PrimaryColor, // 🟢 Título na cor principal (Azul PetCare)
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Escolha uma nota:", fontSize = 16.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(10.dp))

            // ★★★★★ avaliacao
            Row {
                for (i in 1..5) {
                    Text(
                        text = if (i <= rating) "★" else "☆",
                        fontSize = 40.sp,
                        color = Color(0xFFFFD700), // Amarelo fixo para a estrela
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { rating = i }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Comentário:", fontSize = 18.sp, color = PrimaryColor) // 🟢 Label na cor principal

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = comentario,
                onValueChange = { comentario = it },
                singleLine = false,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (loading) {
                CircularProgressIndicator(color = SuccessColor)
            } else {
                Button(
                    onClick = {
                        if (rating == 0) {
                            onError("Selecione uma nota antes de enviar")
                            return@Button
                        }
                        if (vetId.isBlank() || consultaId.isBlank() || tutorUid.isNullOrBlank()) {
                            onError("Erro interno: dados de usuário ou consulta ausentes.")
                            return@Button
                        }

                        loading = true
                        val db = FirebaseFirestore.getInstance()

                        val avaliacaoMap = mapOf(
                            "veterinarioId" to vetId,
                            "nota" to rating.toString(),
                            "comentario" to comentario,
                            "tutorId" to tutorUid,
                            "data" to SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        )

                        coroutineScope.launch {
                            try {
                                // 1. Adiciona a avaliação
                                db.collection("avaliacoes").add(avaliacaoMap).await()

                                // 2. Marca a consulta como 'Avaliada'
                                db.collection("consultas").document(consultaId)
                                    .update("status", "Avaliada").await()

                                // 3. RECALCULA E ATUALIZA A MÉDIA DO VETERINÁRIO
                                recalculateVetRating(db, vetId)

                                loading = false
                                onSuccess()
                            } catch (e: Exception) {
                                loading = false
                                onError("Erro ao salvar avaliação: ${e.message}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessColor) // 🟢 Botão na cor de sucesso/ação (Verde)
                ) {
                    Text(text = "Enviar Avaliação", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}