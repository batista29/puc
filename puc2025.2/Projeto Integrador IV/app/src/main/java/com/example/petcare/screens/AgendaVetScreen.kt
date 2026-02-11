package com.example.petcare.screens

import android.os.Bundle
import android.util.Log
import com.example.petcare.model.Consulta
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent

import com.example.petcare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// ---------------------- ACTIVITY ----------------------
class AgendaVetScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AgendaVetScreenUI() } }
    }
}

// ---------------------- COMPONENTE: BOTTOM MENU ----------------------

@Composable
fun BottomMenu2(){
    val context = LocalContext.current
    val PrimaryColor = Color(0xFF2A5C67)

    NavigationBar(containerColor = Color.White) {
        // Ícones: Home, Agenda, Perfil
        val items = listOf(R.drawable.ic_home, R.drawable.ic_agenda, R.drawable.ic_perfil)
        // Agenda é a aba 1 (índice 1)
        var selected by remember { mutableStateOf(1) }

        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index

                    // Redireciona de acordo com o índice do item
                    when(index) {
                        0 -> context.startActivity(Intent(context, HomeVetScreen::class.java))
                        1 -> context.startActivity(Intent(context, AgendaVetScreen::class.java))
                        2 -> context.startActivity(Intent(context, ProfessionalProfile::class.java))
                    }
                },
                icon = { Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(28.dp)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

// ---------------------- TELA PRINCIPAL DO VETERINÁRIO ----------------------
@Composable
fun AgendaVetScreenUI() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val PrimaryColor = Color(0xFF2A5C67)
    val StatusPendingColor = Color(0xFFD39E00)
    val StatusConfirmedColor = Color(0xFF4CAF50)

    var abaSelecionada by remember { mutableStateOf("pendentes") }
    var todasConsultas by remember { mutableStateOf<List<Consulta>>(emptyList()) }

    val consultasPendentes = todasConsultas.filter { it.status == "Pendente" }
    val consultasConfirmadas = todasConsultas.filter { it.status == "Confirmada" }
    // Consultas Canceladas Removidas

    // 🔹 Escuta dados em tempo real
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        val uid = user?.uid ?: return@LaunchedEffect

        db.collection("consultas")
            .whereEqualTo("veterinarioId", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("AgendaVetScreen", "Erro ao escutar consultas: ${e.message}")
                    return@addSnapshotListener
                }
                todasConsultas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<Consulta>()?.copy(id = doc.id)
                } ?: emptyList()
            }
    }

    // 💡 Ação de Confirmação
    val onConfirmConsulta: (Consulta) -> Unit = { consulta ->
        db.collection("consultas").document(consulta.id).update("status", "Confirmada").addOnSuccessListener {
            Toast.makeText(context, "Consulta de ${consulta.petNome} confirmada!", Toast.LENGTH_SHORT).show()
        }
        if (consulta.petOwnerId.isNotEmpty()) {
            db.collection("usuarios").document(consulta.petOwnerId).collection("agendamentos").document(consulta.id).update("status", "Confirmada")
        }
    }

    // Ação de Excluir
    val onDeleteConsulta: (Consulta) -> Unit = { consulta ->
        db.collection("consultas").document(consulta.id).delete()
        if (consulta.petOwnerId.isNotEmpty()) {
            db.collection("usuarios").document(consulta.petOwnerId).collection("agendamentos").document(consulta.id).delete()
        }
        Toast.makeText(context, "Consulta de ${consulta.petNome} excluída da agenda.", Toast.LENGTH_SHORT).show()
    }


    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomMenu2() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            // LOGO
            Image(
                painter = painterResource(id = R.drawable.logo_petcare),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(8.dp))

            // BOTÕES DE ABA (Apenas Pendentes e Confirmadas)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botão Pendentes
                Button(onClick = { abaSelecionada = "pendentes" }, colors = ButtonDefaults.buttonColors(containerColor = if (abaSelecionada == "pendentes") PrimaryColor else Color(0xFFE0E0E0))) {
                    Text("Pendentes (${consultasPendentes.size})", color = if (abaSelecionada == "pendentes") Color.White else Color(0xFF757575))
                }
                // Botão Confirmadas
                Button(onClick = { abaSelecionada = "confirmadas" }, colors = ButtonDefaults.buttonColors(containerColor = if (abaSelecionada == "confirmadas") PrimaryColor else Color(0xFFE0E0E0))) {
                    Text("Confirmadas (${consultasConfirmadas.size})", color = if (abaSelecionada == "confirmadas") Color.White else Color(0xFF757575))
                }
            }

            // CONTEÚDO ATUALIZADO (Apenas Pendentes e Confirmadas)
            when (abaSelecionada) {
                "pendentes" -> ListaConfirmacaoConsultas(consultasPendentes, onConfirmConsulta, onDeleteConsulta, PrimaryColor, StatusPendingColor)
                "confirmadas" -> ListaConsultasConfirmadas(consultasConfirmadas, onDeleteConsulta, PrimaryColor, StatusConfirmedColor)
                // Caso "canceladas" removido
            }
        }
    }
}

// ---------------------- COMPONENTES DE LISTA ----------------------
// Função ListaConsultasCanceladas REMOVIDA

@Composable
fun ListaConsultasConfirmadas(consultas: List<Consulta>, onDelete: (Consulta) -> Unit, primaryColor: Color, confirmedColor: Color) {
    if (consultas.isEmpty()) {
        Text(
            text = "Nenhuma consulta confirmada ainda.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(consultas) { consulta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3EFF0)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("✅ Consulta Confirmada", fontWeight = FontWeight.Bold, color = confirmedColor)
                            Text("Paciente: ${consulta.petNome}")
                            Text("Serviço: ${consulta.servico}")
                            Text("Data: ${consulta.data}")
                            Text("Hora: ${consulta.hora}")
                        }
                        // Botão de Excluir para Limpeza (opcional)
                        Button(
                            onClick = { onDelete(consulta) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text("Excluir", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ListaConfirmacaoConsultas(
    consultasPendentes: List<Consulta>,
    onConfirm: (Consulta) -> Unit,
    onDeleteConsulta: (Consulta) -> Unit,
    primaryColor: Color,
    pendingColor: Color
) {
    if (consultasPendentes.isEmpty()) {
        Text(
            text = "Nenhuma solicitação de consulta pendente.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp)
        )
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(consultasPendentes) { consulta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("⚠️ Pendente de Confirmação", fontWeight = FontWeight.Bold, color = pendingColor)
                            Text("Paciente: ${consulta.petNome}")
                            Text("Serviço: ${consulta.servico}")
                            Text("Data e Hora: ${consulta.data} às ${consulta.hora}")
                            Button(
                                onClick = {
                                    if (consulta.status == "Pendente") {
                                        onConfirm(consulta)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirmar")
                            }
                        }
                    }
                }
            }
        }
    }
}