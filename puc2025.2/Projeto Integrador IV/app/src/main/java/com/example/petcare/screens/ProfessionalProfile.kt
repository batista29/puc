package com.example.petcare.screens

import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp // Certifique-se de ter essa importação
import com.example.petcare.login_cadastrar.TelaLoginActivity // Se for usá-lo
import androidx.compose.ui.platform.LocalContext   // Para obter o contexto da Activity
import android.content.Intent                      // Para criar o Intent e abrir outra Activity
import androidx.compose.foundation.clickable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text // Ou androidx.compose.material.Text, dependendo da sua versão
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petcare.R
import com.example.petcare.model.Profissional
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.Composable
import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.launch

// ------------------ FUNÇÕES AUXILIARES DE FOTO E FIREBASE ------------------
// (Mantenha as funções uriToBase64, base64ToBitmap e saveBase64ToFirestore se estiverem em outro arquivo, ou as adicione aqui.)

class ProfessionalProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                FullScreen()
            }
        }
    }
}

// ------------------ FUNÇÕES DE BUSCA DE DADOS (getProfissionalLogado) ------------------

suspend fun getProfissionalLogado(): Profissional? {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: return null

    return try {
        val doc = db.collection("usuarios").document(userId).get().await()
        if (doc.exists()) doc.toObject(Profissional::class.java) else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// ------------------ COMPONENTES DA TELA ------------------

@Composable
fun ExibirProfissionalLogado() {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    var profissional by remember { mutableStateOf<Profissional?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        profissional = getProfissionalLogado()
        isLoading = false

        Log.d("AUTH", "UID logado: ${FirebaseAuth.getInstance().currentUser?.uid}")
    }

    when {
        isLoading -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        profissional != null -> {
            Column(modifier = Modifier.padding(start = 20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profissional!!.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                val intent = Intent(context, EditProfessionalProfile::class.java)
                                context.startActivity(intent)
                            }
                    )
                }

                Text(
                    text = profissional!!.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "CEP: ${profissional!!.cep}",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                val listaEspecialidades = profissional!!.especialidades

                Row(
                    modifier = Modifier.horizontalScroll(scrollState),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Especialidades:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    listaEspecialidades.forEach { especialidade ->
                        Text(
                            text = especialidade,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
        }

        else -> {
            Text(
                "Nenhum profissional logado encontrado.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ProfilePicture() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val activity = LocalContext.current as? Activity
    val scope = rememberCoroutineScope()
    val PrimaryColor = Color(0xFF2A5C67)

    var profissional by remember { mutableStateOf<Profissional?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // --- Launcher para Base64 (Selecionar Foto) ---
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null && activity != null) {
                scope.launch {

                    val base64String = uriToBase64(activity, uri)

                    if (base64String.isNotEmpty()) {
                        saveBase64ToFirestore(
                            base64 = base64String,
                            uid = uid,
                            db = db,
                            onSuccess = {
                                profissional = profissional?.copy(fotoUrl = base64String)
                                Toast.makeText(context, "Foto atualizada!", Toast.LENGTH_SHORT).show()
                            },
                            onError = { e ->
                                Toast.makeText(context, "Falha ao salvar foto: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        )
                    }

                }
            }
        }
    )

    // Carrega os dados
    LaunchedEffect(Unit) {
        profissional = getProfissionalLogado()
        isLoading = false
    }

    when {
        isLoading -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        profissional != null -> {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {

                // A parte de base64ToBitmap também deve ser implementada/acessível
                val base64Bitmap = profissional!!.fotoUrl?.let { base64ToBitmap(it)} // Temporariamente null

                val imageModifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)

                if (base64Bitmap != null) {
                    Image(
                        bitmap = base64Bitmap.asImageBitmap(),
                        contentDescription = "Foto do profissional",
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier
                    )
                } else {
                    AsyncImage(
                        model = profissional!!.fotoUrl,
                        contentDescription = "Placeholder ou falha",
                        contentScale = ContentScale.Crop,
                        modifier = imageModifier,
                        error = painterResource(id = R.drawable.ic_perfil) // Exemplo de fallback
                    )
                }

                // Ícone de Lápis (Edit) para indicar que é clicável
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Adicionar Foto",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor)
                        .padding(4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
fun BoxMainInformation() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        ProfilePicture()
        ExibirProfissionalLogado()
    }
}

@Composable
fun ProfileDescription() {
    var profissional by remember { mutableStateOf<Profissional?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        profissional = getProfissionalLogado()
        isLoading = false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp)

            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE2EBF3)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            profissional != null -> {
                Text(
                    text = profissional!!.descricao.ifBlank { "Nenhuma descrição informada. Edite o perfil para te-la" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                Text(
                    "Erro ao carregar descrição.",
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }
        }
    }
}


@Composable
fun InfoLocation() {

    var profissional by remember { mutableStateOf<Profissional?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        profissional = getProfissionalLogado()
        isLoading = false
    }

    when {
        isLoading -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        profissional != null -> {

            val localizacao = LatLng(profissional!!.latitude, profissional!!.longitude)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(localizacao, 12f)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp)
                    // ⭐️ Adicionado o corner/raio de borda
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2EBF3)),
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
                        Marker(
                            state = MarkerState(position = localizacao),
                            title = "Local do profissional",
                            snippet = "Clique para mais detalhes"
                        )
                    }
                }
            }
        }
    }
}

// ⭐️ NOVO COMPONENTE: Botão de Sair
@Composable
fun LogoutButton() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Button(
        onClick = {
            auth.signOut()
            val intent = Intent(context, TelaLoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Sair", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomMenu(){
    val context = LocalContext.current

    NavigationBar(containerColor = Color.White) {
        val items = listOf(R.drawable.ic_home, R.drawable.ic_agenda, R.drawable.ic_perfil)
        var selected by remember { mutableStateOf(2) }

        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index


                    when(index) {
                        0 -> context.startActivity(Intent(context, HomeVetScreen::class.java))
                        1 -> context.startActivity(Intent(context, AgendaVetScreen::class.java))
                        2 -> context.startActivity(Intent(context, ProfessionalProfile::class.java))
                    }

                },
                icon = { Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(28.dp)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2A5C67),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


@Composable
fun FullScreen() {
    Scaffold(
        bottomBar = { BottomMenu() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxMainInformation()
            Spacer(modifier = Modifier.size(80.dp))
            ProfileDescription()
            Spacer(modifier = Modifier.size(30.dp))
            InfoLocation()
            Spacer(modifier = Modifier.size(30.dp))
            LogoutButton() // ⭐️ Botão de sair adicionado
            Spacer(modifier = Modifier.size(30.dp)) // Espaçamento final para o BottomMenu (embora o paddingValues deva cuidar disso)
        }
    }
}

@Preview
@Composable
fun ProfessionalProfilePreview() {
    FullScreen()
}