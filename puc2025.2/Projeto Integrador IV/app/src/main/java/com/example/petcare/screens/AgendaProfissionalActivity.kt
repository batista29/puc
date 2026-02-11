package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.model.Consulta
import com.example.petcare.screens.AvaliacaoActivity
import com.example.petcare.ui.theme.PetCareTheme
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import java.util.Calendar

import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.petcare.screens.ProfessionalProfile

// --------------------------------------------------------
// DATA CLASS E CONSTANTES
// --------------------------------------------------------

data class Consulta(
    val id: String = "",
    val petOwnerId: String = "",
    val tutorNome: String = "",
    val petNome: String = "",
    val servico: String = "",
    val veterinarioNome: String = "",
    val veterinarioId: String = "",
    val data: String = "",
    val hora: String = "",
    val local: String = "",
    val status: String = "Pendente"
)

private val PrimaryColor = Color(0xFF2A5C67)
private val LightGrayColor = Color(0xFFEEEEEE)
private val StatusPendingColor = Color(0xFFFFA726)
private val StatusConfirmedColor = Color(0xFF4CAF50)
private val StatusCancelledColor = Color.Red
private val StatusConcluidaColor = Color(0xFF1E88E5)

// --------------------------------------------------------
// ACTIVITY PRINCIPAL
// --------------------------------------------------------

class PetOwnerAgendaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetCareTheme {
                PetOwnerAgendaScreen()
            }
        }
    }
}

// --------------------------------------------------------
// FUNÇÃO AUXILIAR DE DATA
// --------------------------------------------------------

fun isConsultaPassed(data: String, hora: String): Boolean {
    return try {
        val dateTimeString = "$data $hora"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val consultaDateTime: Date = format.parse(dateTimeString) ?: return false
        consultaDateTime.before(Calendar.getInstance().time)
    } catch (e: Exception) {
        Log.e("DateCheck", "Erro ao parsear data: ${e.message}")
        false
    }
}

// --------------------------------------------------------
// TELA PRINCIPAL
// --------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetOwnerAgendaScreen() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserUid = auth.currentUser?.uid
    val context = LocalContext.current

    var consultas by remember { mutableStateOf(listOf<Consulta>()) }
    var showEditModal by remember { mutableStateOf(false) }
    var showDeleteModal by remember { mutableStateOf(false) }
    var showCancelModal by remember { mutableStateOf(false) }
    var consultaSelecionada by remember { mutableStateOf<Consulta?>(null) }

    LaunchedEffect(currentUserUid) {
        if (currentUserUid != null) {
            db.collection("consultas")
                .whereEqualTo("petOwnerId", currentUserUid)
                .addSnapshotListener { snap, e ->
                    if (e != null) {
                        Log.e("AgendaScreen", "Erro ao carregar consultas: $e")
                        return@addSnapshotListener
                    }
                    if (snap != null) {
                        consultas = snap.documents.mapNotNull { doc ->
                            doc.toObject<Consulta>()?.copy(id = doc.id)
                        }
                    }
                }
        }
    }

    val onEvaluateConsulta: (Consulta) -> Unit = { consulta ->
        val intent = Intent(context, AvaliacaoActivity::class.java).apply {
            putExtra("vetId", consulta.veterinarioId)
            putExtra("consultaId", consulta.id)
        }
        context.startActivity(intent)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_petcare),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Text(
                "Minhas Consultas Agendadas",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyColumn {
                if (consultas.isEmpty()) {
                    item {
                        Text(
                            text = "Nenhuma consulta agendada.",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth().padding(30.dp),
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                items(consultas) { consulta ->
                    ConsultaPetOwnerItem(
                        consulta = consulta,
                        onEdit = {
                            // Só permite edição se for Pendente
                            if (consulta.status == "Pendente") {
                                consultaSelecionada = consulta
                                showEditModal = true
                            } else {
                                Toast.makeText(context, "Consultas confirmadas ou concluídas não podem ser editadas.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDelete = {
                            consultaSelecionada = consulta
                            showDeleteModal = true
                        },
                        onCancel = {
                            // Só permite cancelar se for Confirmada
                            if (consulta.status == "Confirmada") {
                                consultaSelecionada = consulta
                                showCancelModal = true
                            } else {
                                Toast.makeText(context, "Apenas consultas confirmadas podem ser canceladas.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onEvaluate = { onEvaluateConsulta(consulta) }
                    )
                }
            }
        }
    }

    // ------------------ MODAIS --------------------

    if (showEditModal && consultaSelecionada != null) {
        EditModal(
            consulta = consultaSelecionada!!,
            onDismiss = { showEditModal = false },
            onSave = { consultaEditada ->
                val db = FirebaseFirestore.getInstance()
                db.collection("consultas")
                    .document(consultaEditada.id)
                    .update(
                        mapOf(
                            "petNome" to consultaEditada.petNome,
                            "servico" to consultaEditada.servico,
                            "data" to consultaEditada.data,
                            "hora" to consultaEditada.hora
                        )
                    ).addOnSuccessListener {
                        showEditModal = false
                        Toast.makeText(context, "Consulta atualizada!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Erro ao atualizar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        )
    }

    if (showDeleteModal && consultaSelecionada != null) {
        DeleteModal(
            onDismiss = { showDeleteModal = false },
            onConfirm = {
                val db = FirebaseFirestore.getInstance()
                db.collection("consultas")
                    .document(consultaSelecionada!!.id)
                    .delete()
                    .addOnSuccessListener {
                        showDeleteModal = false
                        Toast.makeText(context, "Consulta excluída!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Erro ao excluir: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        )
    }

    if (showCancelModal && consultaSelecionada != null) {
        CancelModal(
            onDismiss = { showCancelModal = false },
            onConfirm = {
                val db = FirebaseFirestore.getInstance()
                db.collection("consultas")
                    .document(consultaSelecionada!!.id)
                    .update("status", "Cancelada")
                    .addOnSuccessListener {
                        Toast.makeText(context, "Consulta Cancelada.", Toast.LENGTH_SHORT).show()
                        showCancelModal = false
                    }.addOnFailureListener {
                        Toast.makeText(context, "Erro ao cancelar: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        )
    }
}

// --------------------------------------------------------
// ITEM DA LISTA
// --------------------------------------------------------

@Composable
fun ConsultaPetOwnerItem(
    consulta: Consulta,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    onEvaluate: () -> Unit
) {
    val isPending = consulta.status == "Pendente"
    val isConfirmed = consulta.status == "Confirmada"
    val isCancelled = consulta.status == "Cancelada"
    val isAvaliada = consulta.status == "Avaliada"

    val isConcluida = isConfirmed && isConsultaPassed(consulta.data, consulta.hora)

    val cardColor = when {
        isPending -> Color(0xFFFFFBE5)
        isConfirmed -> Color(0xFFE3EFF0)
        isCancelled -> Color(0xFFF0E3E3)
        isAvaliada -> Color(0xFFE0F7FA)
        else -> Color(0xFFFFFFFF)
    }

    val statusColor = when {
        isPending -> StatusPendingColor
        isConfirmed -> StatusConfirmedColor
        isCancelled -> StatusCancelledColor
        isAvaliada -> StatusConcluidaColor
        else -> Color.Gray
    }

    val statusText = when {
        isPending -> "Pendente de Confirmação"
        isConfirmed && !isConsultaPassed(consulta.data, consulta.hora) -> "Consulta Confirmada"
        isConcluida -> "Pronta para Avaliação"
        isAvaliada -> "Consulta Avaliada"
        isCancelled -> "Consulta Cancelada"
        else -> "Status Desconhecido"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .background(cardColor, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Text(statusText, fontWeight = FontWeight.Bold, color = statusColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Pet: ${consulta.petNome}", color = PrimaryColor)
        Text("Serviço: ${consulta.servico}")
        Text("Veterinário: ${consulta.veterinarioNome}")
        Text("Data e Hora: ${consulta.data} - ${consulta.hora}", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            when {
                isPending -> {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "editar",
                        tint = PrimaryColor,
                        modifier = Modifier.size(22.dp).clickable { onEdit() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "deletar",
                        tint = StatusCancelledColor,
                        modifier = Modifier.size(22.dp).clickable { onDelete() }
                    )
                }
                isConcluida -> {
                    Button(
                        onClick = onEvaluate,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusConcluidaColor),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("AVALIAR VET", fontSize = 12.sp)
                    }
                }
                isConfirmed -> {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusCancelledColor.copy(alpha = 0.8f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("CANCELAR", fontSize = 12.sp)
                    }
                }
                (isCancelled || isAvaliada) -> {
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.8f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("EXCLUIR", fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------
// MODAIS
// --------------------------------------------------------

@Composable
fun CancelModal(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar Consulta", color = PrimaryColor) },
        text = { Text("Tem certeza que deseja cancelar esta consulta?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(StatusCancelledColor)) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(LightGrayColor)) {
                Text("Voltar", color = Color.Black)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditModal(consulta: Consulta, onDismiss: () -> Unit, onSave: (Consulta) -> Unit) {

    val context = LocalContext.current
    var pet by remember { mutableStateOf(consulta.petNome) }
    var servico by remember { mutableStateOf(consulta.servico) }
    var data by remember { mutableStateOf(consulta.data) }
    var hora by remember { mutableStateOf(consulta.hora) }
    var profissional by remember { mutableStateOf(consulta.veterinarioNome) }
    var consultaId by remember { mutableStateOf(consulta.id) }

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val tutorUid = auth.currentUser?.uid

    val coroutineScope = rememberCoroutineScope()
    val service = remember { AgendamentoService(db, context) }

    // Validações
    val isPetNomeValido = remember(pet) { pet.isNotBlank() && validarApenasLetrasEspacos(pet) }
    val isServicoPreenchido = servico.isNotBlank()
    // Regex para Data: AAAA-MM-DD
    val isDataValida = remember(data) { data.isNotBlank() && data.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) }
    // Regex para Hora: HH:mm
    val isHoraValida = remember(hora) { hora.isNotBlank() && hora.matches(Regex("^\\d{2}:\\d{2}$")) }

    val podeSalvar = isPetNomeValido && isServicoPreenchido && isDataValida && isHoraValida

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val vetUid = consulta.veterinarioId
    var gambiarra by remember { mutableStateOf(" ") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Consulta", color = PrimaryColor) },
        text = {
            Column {
                // 1. PET NOME (Validação de Apenas Letras/Espaços)
                OutlinedTextField(
                    value = pet,
                    onValueChange = { newValue ->
                        // Permite apenas letras e espaços, e restringe a entrada a caracteres válidos
                        if (validarApenasLetrasEspacos(newValue) || newValue.isEmpty()) {
                            pet = newValue
                        } else {
                            Toast.makeText(context, "Nome do Pet deve ter apenas letras.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    label = { Text("Nome do Pet") },
                    isError = pet.isNotEmpty() && !validarApenasLetrasEspacos(pet),
                    supportingText = {
                        if (pet.isNotEmpty() && !validarApenasLetrasEspacos(pet)) {
                            Text("O nome do Pet deve conter apenas letras e espaços.", color = Color.Red)
                        }
                    }
                )

                // 2. SERVIÇO (Validação de Preenchimento)
                OutlinedTextField(
                    value = servico,
                    onValueChange = { servico = it },
                    label = { Text("Serviço") },
                    isError = servico.isEmpty()
                )

                // 3. DATA
                OutlinedTextField(
                    value = data,
                    onValueChange = { data = it },
                    label = { Text("Data (AAAA-MM-DD)") },
                    isError = data.isNotEmpty() && !isDataValida,
                    supportingText = {
                        if (data.isNotEmpty() && !isDataValida) {
                            Text("Formato de data inválido (AAAA-MM-DD).", color = Color.Red)
                        } else if (data.isEmpty()) {
                            Text("Campo obrigatório.", color = Color.Red)
                        }
                    }
                )

                // 4. HORA
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora (HH:mm)") },
                    isError = hora.isNotEmpty() && !isHoraValida,
                    supportingText = {
                        if (hora.isNotEmpty() && !isHoraValida) {
                            Text("Formato de hora inválido (HH:mm).", color = Color.Red)
                        } else if (hora.isEmpty()) {
                            Text("Campo obrigatório.", color = Color.Red)
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (tutorUid.isNullOrBlank()) {
                        Toast.makeText(context, "Faça login para agendar", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val consulta = Consulta(
                        id = consultaId,
                        petOwnerId = tutorUid,
                        veterinarioId = vetUid,
                        veterinarioNome = profissional ?: "",
                        petNome = pet,
                        servico = servico,
                        data = data,
                        hora = hora,
                        status = "Pendente",
                        createdAt = Timestamp.now()
                    )

                    coroutineScope.launch {

                        // 1 — Não pode ser horário passado
                        if (service.horarioEhPassado(data, hora)) {
                            Toast.makeText(context, "Não é possível marcar para um horário passado.", Toast.LENGTH_LONG).show()
                            return@launch
                        }

                        // 2 — Verifica conflitos
                        //Natã
                        val valido = service.validarHorario(vetUid, data, hora)

                        if (!valido) {
                            Toast.makeText(
                                context,
                                "Horário indisponível. O veterinário deve ter pelo menos 1 hora livre entre as consultas.",
                                Toast.LENGTH_LONG
                            ).show()
                            return@launch
                        }
                        // 3 — Salva
                        val ok = service.atualizarConsulta(consulta)

                        if (ok) {
                            Toast.makeText(context, "Agendamento enviado!", Toast.LENGTH_SHORT).show()
                            gambiarra=""
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(PrimaryColor),
                enabled = podeSalvar // Habilita o botão somente se tudo for válido
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(LightGrayColor)) {
                Text("Cancelar", color = Color.Black)
            }
        }
    )
}

@Composable
fun DeleteModal(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir Consulta", color = PrimaryColor) },
        text = { Text("Tem certeza que deseja excluir esta consulta permanentemente?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(StatusCancelledColor)) {
                Text("Excluir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(LightGrayColor)) {
                Text("Cancelar", color = Color.Black)
            }
        }
    )
}

// =======================================================
// FUNÇÃO AUXILIAR DE VALIDAÇÃO
// =======================================================

/**
 * Verifica se a string contém apenas letras, espaços e acentos comuns.
 */
fun validarApenasLetrasEspacos(texto: String): Boolean {
    // Permite letras, espaços e acentos comuns. Permite string vazia.
    return texto.matches(Regex("^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]*$"))
}