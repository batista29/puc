package com.example.petcare.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.ui.theme.PetCareTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.Locale
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.petcare.login_cadastrar.TelaLoginActivity

import com.example.petcare.screens.ProfessionalProfile
import com.example.petcare.screens.AgendaVetScreen

//------------------------------------------------
// CORES
//------------------------------------------------

private val PrimaryColor = Color(0xFF2A5C67)
private val CardBackgroundColor = Color(0xFFEFF7F5)

//------------------------------------------------
// MODELOS
//------------------------------------------------

data class Consulta(
    val id: String = "",
    val tutorNome: String = "",
    val petNome: String = "",
    val data: String = "",
    val hora: String = "",
    val local: String = "",
    val veterinarioId: String = "",
    val status: String = ""
)

data class Avaliacao(
    val comentario: String = "",
    val nota: String = "",
    val tutorId: String = "",
    val veterinarioId: String = ""
)

//------------------------------------------------
// ACTIVITY PRINCIPAL
//------------------------------------------------

class HomeVetScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetCareTheme {
                HomeVetScreenUI()
            }
        }
    }
}

//------------------------------------------------
// FUNÇÃO DE LOGOUT (Mantida por boas práticas)
//------------------------------------------------

fun PerformLogout(context: android.content.Context) {
    val auth = FirebaseAuth.getInstance()
    auth.signOut()

    val intent = Intent(context, TelaLoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? ComponentActivity)?.finish()
}


//------------------------------------------------
// UI PRINCIPAL
//------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeVetScreenUI() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    var nomeVeterinario by remember { mutableStateOf("Profissional") }
    var mediaAvaliacoes by remember { mutableStateOf<Double?>(null) }
    var avaliacoes by remember { mutableStateOf<List<Avaliacao>>(emptyList()) }
    var consultas by remember { mutableStateOf<List<Consulta>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    //------------------------------------------------
    // CARREGAR DADOS
    //------------------------------------------------
    LaunchedEffect(uid) {
        if (uid == null) {
            loading = false
            return@LaunchedEffect
        }

        loading = true

        try {
            val userDoc = db.collection("usuarios").document(uid).get().await()
            nomeVeterinario = userDoc.getString("nome") ?: "Profissional"
            mediaAvaliacoes = userDoc.getDouble("nota")

            avaliacoes = db.collection("avaliacoes")
                .whereEqualTo("veterinarioId", uid)
                .get().await()
                .documents.mapNotNull { it.toObject<Avaliacao>() }

            consultas = db.collection("consultas")
                .whereEqualTo("veterinarioId", uid)
                .get().await()
                .documents.mapNotNull { it.toObject<Consulta>()?.copy(id = it.id) }

        } catch (e: Exception) {
            Log.e("HomeVetScreen", "Erro: ${e.message}")
        }

        loading = false
    }

    val consultasConfirmadas = consultas
        .filter { it.status == "Confirmada" }
        .sortedBy { it.data + " " + it.hora }

    //------------------------------------------------
    // UI
    //------------------------------------------------

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomMenu1(selectedItemIndex = 0) }
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                //------------------------------------------------
                // HEADER (LOGOTIPO CENTRALIZADO)
                //------------------------------------------------

                item {
                    Spacer(Modifier.height(20.dp))

                    // 1. Removemos o Row(horizontalArrangement = Arrangement.SpaceBetween)
                    // 2. Usamos Box para centralizar o Image
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_petcare),
                            contentDescription = null,
                            modifier = Modifier.size(170.dp)
                        )
                        // O IconButton de Sair foi removido daqui
                    }

                    Spacer(Modifier.height(10.dp))

                    //------------------------------------------------
                    // SAUDAÇÃO E AVALIAÇÃO (Mantido)
                    //------------------------------------------------

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Olá, Dr(a). $nomeVeterinario 👋",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                            Text(
                                text = String.format(Locale.getDefault(), "%.1f", mediaAvaliacoes ?: 0.0),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    Divider()
                    Spacer(Modifier.height(20.dp))
                }

                //------------------------------------------------
                // CONSULTAS CONFIRMADAS
                //------------------------------------------------

                item { SectionTitle("Próximas Consultas Confirmadas") }
                item { Spacer(Modifier.height(10.dp)) }

                item {
                    if (consultasConfirmadas.isEmpty()) {
                        EmptyText("Nenhuma consulta confirmada.")
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(consultasConfirmadas) { ConsultaCard(it) }
                        }
                    }
                }

                //------------------------------------------------
                // AVALIAÇÕES
                //------------------------------------------------

                item {
                    Spacer(Modifier.height(30.dp))
                    Divider()
                    Spacer(Modifier.height(20.dp))
                    SectionTitle("Avaliações Recentes (${avaliacoes.size})")
                    Spacer(Modifier.height(10.dp))
                }

                item {
                    if (avaliacoes.isEmpty()) {
                        EmptyText("Nenhuma avaliação ainda.")
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(avaliacoes.take(5)) { AvaliacaoCard(it) }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

//------------------------------------------------
// BOTTOM MENU (Mantido o uso de Intents)
//------------------------------------------------

@Composable
fun BottomMenu1(selectedItemIndex: Int) {
    val context = LocalContext.current

    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_agenda,
        R.drawable.ic_perfil
    )

    NavigationBar(containerColor = Color.White) {
        icons.forEachIndexed { index, icon ->

            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    when (index) {
                        0 -> { /* já está na Home */ }
                        // Usando intents conforme o código original
                        1 -> context.startActivity(Intent(context, AgendaVetScreen::class.java))
                        2 -> context.startActivity(Intent(context, ProfessionalProfile::class.java))
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryColor,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

//------------------------------------------------
// COMPONENTES (Mantidos)
//------------------------------------------------

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = PrimaryColor
    )
}

@Composable
fun EmptyText(msg: String) {
    Text(
        text = msg,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    )
}

@Composable
fun AvaliacaoCard(avaliacao: Avaliacao) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = avaliacao.nota,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = avaliacao.comentario,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ConsultaCard(consulta: Consulta) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = consulta.petNome,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Column {
                Text(consulta.data, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(consulta.hora, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(consulta.local, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}