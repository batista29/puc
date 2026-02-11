package com.example.petcare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petcare.model.Profissional
import com.example.petcare.model.Avaliacao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.example.petcare.R

// ---------------- Funções Firebase ----------------
suspend fun getProfissionalByUid(uid: String): Profissional? {
    return try {
        val doc = FirebaseFirestore.getInstance().collection("usuarios").document(uid).get().await()
        if (doc.exists()) doc.toObject(Profissional::class.java) else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getAvaliacoesByVetUid(vetUid: String): List<Avaliacao> {
    return try {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("avaliacoes")
            .whereEqualTo("veterinarioId", vetUid)
            .get()
            .await()
        snapshot.documents.mapNotNull { it.toObject(Avaliacao::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

// ---------------- Tela Detalhes ----------------
// ---------------- Tela Detalhes ----------------
@Composable
fun DetalhesVetScreen(navController: NavController, vetUid: String) {
    var profissional by remember { mutableStateOf<Profissional?>(null) }
    var avaliacoes by remember { mutableStateOf<List<Avaliacao>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(vetUid) {
        try {
            isLoading = true
            profissional = getProfissionalByUid(vetUid)
            avaliacoes = getAvaliacoesByVetUid(vetUid)
            isLoading = false
        } catch (e: Exception) {
            errorMsg = e.message ?: "Erro ao carregar"
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // ---------------- Header ----------------
        Box(
            modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF2A5C67)
                    )
                }
                Text(
                    text = profissional?.nome ?: "Detalhes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2A5C67)
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2A5C67))
            }
            return@Column
        }

        if (errorMsg != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erro: $errorMsg", color = Color.Red)
            }
            return@Column
        }

        if (profissional == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Veterinário não encontrado.", color = Color.Red)
            }
            return@Column
        }

        // --- Início do conteúdo que rola ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // ---------------- Foto e informações (Horizontal) ----------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto
                AsyncImage(
                    model = profissional!!.fotoUrl.ifBlank { null },
                    contentDescription = "Foto do profissional",
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )

                Spacer(Modifier.width(16.dp))

                // Informações (à direita da foto)
                Column(modifier = Modifier.weight(1f)) {
                    Text(profissional!!.nome, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Email: ${profissional!!.email}", fontSize = 14.sp, color = Color.Gray)
                    Text("Especialidades: ${profissional!!.especialidades.joinToString(", ")}", fontSize = 14.sp)
                    Text("CEP: ${profissional!!.cep}", fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------------- Nota média (Maior e Centralizada) ----------------
            val notas = avaliacoes.mapNotNull { it.nota.toFloatOrNull() }
            val notaMedia = if (notas.isNotEmpty()) notas.average() else 0.0

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "%.1f".format(notaMedia),
                        fontSize = 48.sp, // Aumentado para 48.sp
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2A5C67)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(48.dp) // Ícone maior
                    )
                }
                Text("${avaliacoes.size} Avaliações", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(Modifier.height(24.dp))

            // ---------------- Descrição ----------------
            // Título da Descrição (Novo!)
            Text("Descrição", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE2EBF3), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    profissional!!.descricao.ifBlank { "Nenhuma descrição disponível." },
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(16.dp))

            // ---------------- Comentários ----------------
            Text("Comentários", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (avaliacoes.isEmpty()) {
                Text("Nenhum comentário disponível.", fontSize = 14.sp, color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(avaliacoes) { avaliacao ->
                        val notaComentario = avaliacao.nota.toIntOrNull() ?: 0
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(notaComentario) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(avaliacao.comentario, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------------- Mapa ----------------
            val localizacao = LatLng(profissional!!.latitude, profissional!!.longitude)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(localizacao, 12f)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFFE2EBF3), RoundedCornerShape(8.dp)),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Localização",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(state = MarkerState(localizacao), title = "Local do profissional")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------------- Bottom Menu ----------------

        }
    }
}
// ---------------- Bottom Navigation ----------------



