package com.example.petcare.screens

// ------------------ IMPORTS ------------------
import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import com.google.firebase.Timestamp // Adicione esta linha
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.petcare.R
import com.example.petcare.PetOwnerAgendaScreen // se existir (mantive caso você queira)
import com.example.petcare.AgendamentoService
import com.example.petcare.model.Consulta
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// ------------------ MODELS & CONSTANTS ------------------

data class NavItemData(val label: String, val icon: Int)

data class Profissional(
    // ESTA PROPRIEDADE É OBRIGATÓRIA PARA O copy(uid = ...) FUNCIONAR
    val uid: String = "",

    val nome: String = "",
    val cidade: String = "",
    val endereco: String = "",
    val nota: Double = 0.0,
    val fotoUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val disponibilidade: String = "",
    val tipo: String = "",
    val email: String = "",
    val telefone: String = "",
    val especialidades: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)
data class ConsultaPetOwner(
    val petNome: String = "",
    val servico: String = "Consulta", // sempre "Consulta"
    val data: String = "",
    val hora: String = "",
    val veterinarioNome: String = "",
    val consultaId: String = ""
)

private val PrimaryColor = Color(0xFF2A5C67)
private const val DISTANCE_RADIUS_KM = 15.0
// LISTA DE FALLBACK IMAGES
private val DEFAULT_PET_IMAGES = listOf(
    R.drawable.pet1,
    R.drawable.pet2,
    R.drawable.pet3
)

@Composable
fun getPetFallbackImage(index: Int) = painterResource(
    id = DEFAULT_PET_IMAGES[index % DEFAULT_PET_IMAGES.size]
)

// ------------------ PARAM HOLDER (simples) ------------------
object SearchParamsHolder {
    var query: String = ""
    var especialidade: String? = null
    var cidade: String? = null
    var tags: List<String> = emptyList()
}

// ------------------ ACTIVITY ------------------
class HomePetOwnerScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                AppContent()
            }
        }
    }
}

// ------------------ COMPOSABLE PRINCIPAL ------------------

@Composable
fun AppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavItemData("home", R.drawable.ic_home),
        NavItemData("agenda", R.drawable.ic_agenda),
        NavItemData("chat", R.drawable.ic_chat),
        NavItemData("perfil", R.drawable.ic_perfil)
    )

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEach { nav ->
                    NavigationBarItem(
                        selected = currentRoute == nav.label,
                        onClick = {
                            navController.navigate(nav.label) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = nav.icon),
                                contentDescription = nav.label,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        label = {},
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryColor,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        AppNavHost(navController = navController, modifier = Modifier.padding(padding))
    }
}

// ------------------ APP NAV HOST ------------------

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreenContent(navController = navController) }
        composable("search") { SearchResultsScreen(navController = navController) }
        composable("perfil") { ProfileScreenContent() }
        composable("agenda") { PetOwnerAgendaScreen() }
        composable("chat") { ChatbotScreen() }

        // ROTA DE DETALHES DO VETERINÁRIO (Novo!)
        composable("detalhes/{vetUid}") { backStack ->
            val vetUid = backStack.arguments?.getString("vetUid")
            if (vetUid != null) {
                DetalhesVetScreen(navController = navController, vetUid = vetUid)
            } else {
                // fallback
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Veterinário inválido", color = Color.Red)
                }
            }
        }

        // ROTA DE AGENDAMENTO: recebe vetUid
        composable("agendar/{vetUid}") { backStack ->
            val vetUid = backStack.arguments?.getString("vetUid")
            if (vetUid != null) {
                AgendarScreen(navController = navController, vetUid = vetUid)
            } else {
                // fallback
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Profissional inválido", color = Color.Red)
                }
            }
        }
    }
}



// ------------------ HOME SCREEN CONTENT ------------------

@Composable
fun HomeScreenContent(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<Location?>(null) }
    var profissionaisTop by remember { mutableStateOf<List<Profissional>>(emptyList()) }
    var profissionaisPerto by remember { mutableStateOf<List<Profissional>>(emptyList()) }
    var profissionaisAll by remember { mutableStateOf<List<Profissional>>(emptyList()) }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val favoritedUids = remember { mutableStateMapOf<String, Boolean>() }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    val toggleFavorite: (String) -> Unit = { uid ->
        if (favoritedUids.contains(uid)) {
            favoritedUids.remove(uid)
        } else {
            favoritedUids[uid] = true
        }
    }
    val especialidadesOptions = listOf("Todas", "Clínico Geral", "Cirurgião", "Dermatologia", "Cardiologia", "Oftalmologia", "Ortopedia", "Odontologia", "Neurologia", "Oncologia", "Endocrinologia", "Nutrição", "Anastesia","Comportamental (Etologia)", "Exótico")
    val tagsOptions = listOf("Atende online", "Atende em domicílio", "Atende no consultório", "Aceita plano de saúde pet", "Urgência", "24h", "Finais de semana", "Atende cães", "Atende gatos", "Atende exóticos")

    var especialidadeSelecionada by remember { mutableStateOf("Todas") }
    var tagsSelecionadas by remember { mutableStateOf<List<String>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.any { it.value }
        if (granted) {
            coroutineScope.launch {
                loadProfessionalsBasedOnLocation(context, fusedLocationClient) { location, pertoList ->
                    userLocation = location
                    profissionaisPerto = pertoList
                }
            }
        } else {
            profissionaisPerto = emptyList()
        }
    }

    // Carrega profissionais do Firestore
    LaunchedEffect(Unit) {
        try {
            val all = fetchProfissionaisSuspend()
            profissionaisAll = all
            profissionaisTop = all.sortedByDescending { it.nota }.take(5)

            val fineGranted = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val coarseGranted = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (fineGranted || coarseGranted) {
                loadProfessionalsBasedOnLocation(context, fusedLocationClient) { location, pertoList ->
                    userLocation = location
                    profissionaisPerto = pertoList
                }
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Erro ao inicializar HomeScreen: ${e.message}", e)
            profissionaisTop = emptyList()
            profissionaisPerto = emptyList()
            profissionaisAll = emptyList()
        }
    }

    val listaFiltradaTop by remember(profissionaisTop, especialidadeSelecionada, tagsSelecionadas, showOnlyFavorites, favoritedUids.toMap()) {
        mutableStateOf(
            profissionaisTop.filter { prof ->
                val okEspecialidade = (especialidadeSelecionada == "Todas") || prof.especialidades.any { it.equals(especialidadeSelecionada, ignoreCase = true) }
                val okTags = tagsSelecionadas.isEmpty() || tagsSelecionadas.all { tag -> prof.tags.any { it.equals(tag, ignoreCase = true) } }
                val okFavorite = !showOnlyFavorites || (favoritedUids[prof.uid] == true)
                okEspecialidade && okTags && okFavorite
            }
        )
    }

    val listaFiltradaPerto by remember(profissionaisPerto, especialidadeSelecionada, tagsSelecionadas, showOnlyFavorites, favoritedUids.toMap()) {
        mutableStateOf(
            profissionaisPerto.filter { prof ->
                val okEspecialidade = (especialidadeSelecionada == "Todas") || prof.especialidades.any { it.equals(especialidadeSelecionada, ignoreCase = true) }
                val okTags = tagsSelecionadas.isEmpty() || tagsSelecionadas.all { tag -> prof.tags.any { it.equals(tag, ignoreCase = true) } }
                val okFavorite = !showOnlyFavorites || (favoritedUids[prof.uid] == true)
                okEspecialidade && okTags && okFavorite
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 12.dp) // padding geral na tela
    ) {
        // Logo + SearchBar + filtros
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(4.dp)) // Reduzi de 6dp para 4dp

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.logo_petcare),
                    contentDescription = null,
                    modifier = Modifier.size(180.dp) // tamanho do logo mantido
                )
            }

            Spacer(Modifier.height(6.dp)) // reduzido um pouco

            // Search bar com botão de busca
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, PrimaryColor),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            decorationBox = { inner ->
                                if (searchQuery.text.isEmpty()) {
                                    Text(
                                        text = "Pesquisar",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                                inner()
                            }
                        )

                        IconButton(onClick = {
                            SearchParamsHolder.query = searchQuery.text.trim()
                            SearchParamsHolder.especialidade = if (especialidadeSelecionada == "Todas") null else especialidadeSelecionada
                            SearchParamsHolder.cidade = null
                            SearchParamsHolder.tags = tagsSelecionadas
                            navController.navigate("search")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = PrimaryColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp)) // reduzido de 12dp

            // FILTROS: Especialidade (chips) e Tags (chips)
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Especialidade", fontWeight = FontWeight.SemiBold, color = PrimaryColor)
                Spacer(Modifier.height(4.dp)) // reduzido

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp) // reduzido
                ) {
                    especialidadesOptions.forEach { esp ->
                        val selected = esp.equals(especialidadeSelecionada, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (selected) PrimaryColor else Color.Transparent,
                            border = BorderStroke(1.dp, PrimaryColor),
                            modifier = Modifier
                                .clickable { especialidadeSelecionada = esp }
                        ) {
                            Text(
                                text = esp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), // vertical reduzido
                                color = if (selected) Color.White else PrimaryColor,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp)) // reduzido

                Text("Filtros adicionais", fontWeight = FontWeight.SemiBold, color = PrimaryColor)
                Spacer(Modifier.height(4.dp)) // reduzido
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp) // reduzido
                ) {
                    tagsOptions.forEach { tag ->
                        val selected = tag in tagsSelecionadas
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (selected) PrimaryColor else Color.Transparent,
                            border = BorderStroke(1.dp, PrimaryColor),
                            modifier = Modifier
                                .clickable { tagsSelecionadas = if (selected) tagsSelecionadas - tag else tagsSelecionadas + tag }
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), // vertical reduzido
                                color = if (selected) Color.White else PrimaryColor,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // reduzido

            // SWITCH DE FAVORITOS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mostrar apenas favoritos", fontWeight = FontWeight.SemiBold, color = PrimaryColor)
                Switch(
                    checked = showOnlyFavorites,
                    onCheckedChange = { showOnlyFavorites = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = PrimaryColor)
                )
            }

            Spacer(Modifier.height(8.dp)) // reduzido
        }

        // SCROLLABLE: Profissionais
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // MELHORES AVALIAÇÕES
            Text(
                "Melhores avaliações",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(Modifier.height(14.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(200.dp)
            ) {
                if (listaFiltradaTop.isEmpty()) {
                    item {
                        Text(
                            "Nenhum profissional com avaliação ainda.",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 40.dp)
                        )
                    }
                }
                items(listaFiltradaTop) { prof ->
                    Card(
                        modifier = Modifier
                            .width(140.dp)
                            .clickable {
                                navController.navigate("detalhes/${prof.uid}")
                            },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = prof.fotoUrl.ifBlank { null },
                                contentDescription = prof.nome,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                error = getPetFallbackImage(profissionaisTop.indexOf(prof).coerceAtLeast(0)),
                                placeholder = getPetFallbackImage(profissionaisTop.indexOf(prof).coerceAtLeast(0))
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(prof.nome, fontSize = 14.sp, color = PrimaryColor, maxLines = 1)
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("%.1f".format(prof.nota), fontSize = 14.sp, color = PrimaryColor)
                                Spacer(Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Button(onClick = { navController.navigate("agendar/${prof.uid}") }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)) {
                                Text("Agendar", color = Color.White)
                            }

                            Spacer(Modifier.height(6.dp))
                            Text(prof.endereco.ifBlank { prof.cidade }, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }
                    }
                }

            }

            Spacer(Modifier.height(10.dp))

            Text("Profissionais perto de você", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
            Spacer(Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                if (listaFiltradaPerto.isEmpty()) {
                    item { Text("Nenhum profissional encontrado a 15km.", color = Color.Gray, modifier = Modifier.padding(top = 40.dp)) }
                }
                items(listaFiltradaPerto) { prof ->
                    Card(modifier = Modifier.width(340.dp).clickable {
                        navController.navigate("detalhes/${prof.uid}")
                    }, shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(6.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                AsyncImage(model = prof.fotoUrl.ifBlank { null }, contentDescription = prof.nome, modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop, error = getPetFallbackImage(listaFiltradaPerto.indexOf(prof).coerceAtLeast(0)), placeholder = getPetFallbackImage(listaFiltradaPerto.indexOf(prof).coerceAtLeast(0)))
                                Spacer(Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(prof.nome, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryColor, modifier = Modifier.weight(1f), maxLines = 1)
                                    IconButton(onClick = { if (favoritedUids.contains(prof.uid)) favoritedUids.remove(prof.uid) else favoritedUids[prof.uid] = true }) {
                                        Icon(painter = painterResource(id = R.drawable.ic_heart), contentDescription = "Favoritar", tint = if (favoritedUids[prof.uid] == true) Color.Red else Color.Gray, modifier = Modifier.size(24.dp))
                                    }
                                }

                                Spacer(Modifier.height(6.dp))

                                if (prof.disponibilidade.isNotBlank()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(painter = painterResource(id = R.drawable.ic_agenda), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text(prof.disponibilidade, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Spacer(Modifier.height(6.dp))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Nota: %.1f".format(prof.nota), fontSize = 14.sp, color = PrimaryColor)
                                    Spacer(Modifier.width(6.dp))
                                    Image(painter = painterResource(id = R.drawable.star), contentDescription = null, modifier = Modifier.size(14.dp))
                                }

                                Spacer(Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(painter = painterResource(id = R.drawable.ic_location), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(prof.endereco.ifBlank { prof.cidade }, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                                    Spacer(Modifier.width(6.dp))
                                }
                            }

                            Button(onClick = {
                                // NAVEGA para a tela de agendamento, passando o uid do vet
                                navController.navigate("agendar/${prof.uid}")
                            }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Text("Agendar")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

// ------------------ AGENDAR SCREEN ------------------


fun validarApenasLetrasEspacos(texto: String): Boolean {
    // Permite letras, espaços e acentos comuns. Permite string vazia.
    return texto.matches(Regex("^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]*$"))
}
fun validarSeNaoApenasNumeros(texto: String): Boolean {
    val trimmedText = texto.trim()
    if (trimmedText.isEmpty()) return true // É opcional, então vazio é válido

    // Regex que verifica se a string contem QUALQUER letra (maiúscula ou minúscula)
    // Se contiver qualquer letra, retorna true (válido).
    if (trimmedText.matches(Regex(".*[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ].*"))) {
        return true
    }

    // Verifica se é composta APENAS por dígitos (0-9) e retorna false (inválido)
    return !trimmedText.matches(Regex("^[0-9]+$"))
}

// =======================================================
// TELA PRINCIPAL
// =======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(
    navController: NavController,
    vetUid: String
) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val tutorUid = auth.currentUser?.uid

    val PrimaryColor = Color(0xFF2A5C67)
    val StatusPendingColor = Color(0xFFFFA726)
    val StatusCancelledColor = Color.Red

    val service = remember { AgendamentoService(db, context) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // ---------- ESTADOS ----------
    var vet by remember { mutableStateOf<Profissional?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Formulário
    var petNome by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var planoSaude by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var especialidadeSelecionada by remember { mutableStateOf("") }

    // ---------- VALIDAÇÃO ----------
    val isPetNomeValido = remember(petNome) { petNome.isNotBlank() && validarApenasLetrasEspacos(petNome) }
    val isDataPreenchida = data.isNotBlank()
    val isHoraPreenchida = hora.isNotBlank()
    // NOVA VALIDAÇÃO
    val isPlanoSaudeValido = remember(planoSaude) { validarSeNaoApenasNumeros(planoSaude) }


    // ---------- PICKERS ----------
    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        context,
        { _, y, m, d -> data = "%04d-%02d-%02d".format(y, m + 1, d) },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePicker = TimePickerDialog(
        context,
        { _, h, min -> hora = "%02d:%02d".format(h, min) },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    // ---------- BUSCA DO PROFISSIONAL ----------
    LaunchedEffect(vetUid) {
        loading = true
        try {
            val snap = db.collection("usuarios").document(vetUid).get().await()

            if (snap.exists()) {
                val profissional = snap.toObject(Profissional::class.java)
                    ?.copy(uid = snap.id)
                    ?: Profissional(uid = snap.id)

                vet = profissional.copy(
                    especialidades = profissional.especialidades ?: emptyList()
                )

                especialidadeSelecionada =
                    profissional.especialidades?.firstOrNull() ?: "Serviço Padrão"

            } else {
                error = "Profissional não encontrado"
            }
        } catch (e: Exception) {
            error = e.message ?: "Erro desconhecido"
        } finally {
            loading = false
        }
    }

    // ---------- ESTADOS DE TELA ----------
    if (loading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Erro: $error", color = StatusCancelledColor)
        }
        return
    }

    val profissional = vet ?: return

    // ---------- LAYOUT ----------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Agendando com:", fontSize = 18.sp, color = Color.Gray)

        Text(
            profissional.nome ?: "Veterinário",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = PrimaryColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Card de contato
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = StatusPendingColor)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Dúvidas sobre o agendamento?", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Contato: ${profissional.telefone ?: "Não informado"}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // NOME DO PET - CORRIGIDO PARA ACEITAR APENAS LETRAS
        OutlinedTextField(
            value = petNome,
            onValueChange = { newValue ->
                if (validarApenasLetrasEspacos(newValue)) {
                    petNome = newValue
                } else if (newValue.isNotEmpty()) {
                    Toast.makeText(context, "Nome do Pet deve ter apenas letras.", Toast.LENGTH_SHORT).show()
                }
            },
            label = { Text("Nome do pet") },
            modifier = Modifier.fillMaxWidth(),
            isError = petNome.isNotBlank() && !validarApenasLetrasEspacos(petNome),
            supportingText = {
                if (!isPetNomeValido && petNome.isNotBlank()) {
                    Text("O nome do Pet deve conter apenas letras e espaços.", color = Color.Red)
                } else if (petNome.isBlank() && !isPetNomeValido) {
                    Text("Campo obrigatório.", color = Color.Red)
                }
            }
        )
        Spacer(Modifier.height(12.dp))

        // ESPECIALIDADE - CORRIGIDO MECANISMO DE ABERTURA DO DROPDOWN
        Box(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = especialidadeSelecionada,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("Especialidade") },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Abrir Especialidades"
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                profissional.especialidades?.forEach { esp ->
                    DropdownMenuItem(
                        text = { Text(esp) },
                        onClick = {
                            especialidadeSelecionada = esp
                            expanded = false
                        }
                    )
                }
                if (profissional.especialidades.isNullOrEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Nenhuma especialidade disponível") },
                        onClick = {}
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {

            // CAMPO DATA (Obrigatório)
            OutlinedTextField(
                value = data,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f),
                label = { Text("Data") },
                trailingIcon = {
                    IconButton({ datePicker.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Selecionar Data")
                    }
                },
                isError = !isDataPreenchida,
                supportingText = {
                    if (!isDataPreenchida) {
                        Text("Data obrigatória.", color = Color.Red)
                    }
                }
            )

            // CAMPO HORA (Obrigatório)
            OutlinedTextField(
                value = hora,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f),
                label = { Text("Hora") },
                trailingIcon = {
                    IconButton({ timePicker.show() }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Selecionar Hora")
                    }
                },
                isError = !isHoraPreenchida,
                supportingText = {
                    if (!isHoraPreenchida) {
                        Text("Hora obrigatória.", color = Color.Red)
                    }
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        // PLANO DE SAÚDE (Opcional, mas não pode ser APENAS números) - CORRIGIDO
        OutlinedTextField(
            value = planoSaude,
            onValueChange = { planoSaude = it },
            label = { Text("Plano de Saúde (Opcional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            isError = !isPlanoSaudeValido,
            supportingText = {
                if (!isPlanoSaudeValido) {
                    Text("O nome do Plano de Saúde deve conter letras ou ficar vazio.", color = Color.Red)
                }
            }
        )

        Spacer(Modifier.height(24.dp))

        // ---------------- BOTÃO -----------------
        Button(
            onClick = {
                if (tutorUid.isNullOrBlank()) {
                    Toast.makeText(context, "Faça login para agendar", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // VALIDAÇÃO FINAL INCLUINDO PLANO DE SAÚDE
                if (!isPetNomeValido || !isDataPreenchida || !isHoraPreenchida || !isPlanoSaudeValido) {
                    Toast.makeText(context, "Preencha e corrija todos os campos obrigatórios!", Toast.LENGTH_LONG).show()
                    return@Button
                }

                val consulta = Consulta(
                    id = "",
                    petOwnerId = tutorUid,
                    veterinarioId = vetUid,
                    veterinarioNome = profissional.nome ?: "",
                    petNome = petNome,
                    servico = especialidadeSelecionada,
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
                    val ok = service.criarConsulta(consulta)

                    if (ok) {
                        Toast.makeText(context, "Agendamento enviado!", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            enabled = isPetNomeValido && isDataPreenchida && isHoraPreenchida && isPlanoSaudeValido // Todos devem ser válidos
        ) {
            Text("Confirmar", color = Color.White)
        }

        Spacer(Modifier.height(16.dp))
    }
}




// ------------------ SEARCH RESULTS SCREEN (HEADER SIMPLES) ------------------

private val LightGrayBackground = Color(0xFFF8F8F8) // Cor de fundo suave

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController) {
    val queryFiltro = SearchParamsHolder.query.trim().lowercase()
    val especialidadeFiltro = SearchParamsHolder.especialidade
    val tagsFiltro = SearchParamsHolder.tags

    var profissionais by remember { mutableStateOf<List<Profissional>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val all = fetchProfissionaisSuspend()

            profissionais = all.filter { prof ->
                val okTipo = prof.tipo != "tutor"
                val okQuery = queryFiltro.isEmpty() || prof.nome.lowercase().contains(queryFiltro)
                val okEspecialidade = especialidadeFiltro == null ||
                        prof.especialidades.any { it.equals(especialidadeFiltro, ignoreCase = true) }
                val okTags = tagsFiltro.isEmpty() ||
                        tagsFiltro.all { tagDesejada ->
                            prof.tags.any { it.equals(tagDesejada, ignoreCase = true) }
                        }

                okTipo && okQuery && okEspecialidade && okTags
            }

            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            errorMsg = e.message ?: "Erro ao buscar"
            Log.e("SearchResultsScreen", "Erro na busca: ${e.message}", e)
        }
    }

    // --- INTERFACE DO USUÁRIO ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados da Busca", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.DarkGray) }, // 🌟 FONTE ESCURA
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.DarkGray) // 🌟 SETA ESCURA
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // 🌟 FUNDO BRANCO
                    titleContentColor = Color.DarkGray // Garante a cor escura
                )
            )
        },
        containerColor = LightGrayBackground // Fundo suave para o conteúdo
    ) { paddingValues ->

        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
                return@Column
            }

            if (errorMsg != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Erro: $errorMsg", color = Color.Red)
                }
                return@Column
            }

            if (profissionais.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Nenhum resultado encontrado com os filtros selecionados.",
                        color = Color.Gray,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                return@Column
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text("Encontrados: ${profissionais.size} profissionais", fontWeight = FontWeight.SemiBold, color = PrimaryColor)
                    Spacer(Modifier.height(8.dp))
                }

                items(profissionais) { prof ->
                    ProfissionalResultCard(prof, navController, profissionais.indexOf(prof))
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

// ------------------ CARD DE RESULTADOS OTIMIZADO ------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfissionalResultCard(prof: Profissional, navController: NavController, index: Int) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detalhes/${prof.uid}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 1. LINHA PRINCIPAL (FOTO, NOME, ESPECIALIDADE e ENDEREÇO)
            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = prof.fotoUrl.ifBlank { null },
                    contentDescription = prof.nome,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = getPetFallbackImage(index),
                    placeholder = getPetFallbackImage(index)
                )
                Spacer(Modifier.width(16.dp))

                // Informações Textuais
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        prof.nome,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = PrimaryColor
                    )

                    // 🌟 EXIBE TODAS AS ESPECIALIDADES
                    Text(
                        prof.especialidades.joinToString(", ").ifBlank { "Profissional" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray // Cor de destaque para especialidades
                    )

                    Spacer(Modifier.height(4.dp))

                    // Endereço/Local
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            prof.endereco.ifBlank { prof.cidade },
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 2. TAGS (AJUSTADAS E REFINADAS)
            if (prof.tags.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Divider(color = LightGrayBackground, thickness = 1.dp)
                Spacer(Modifier.height(12.dp))

                // Limita a exibição a 3 tags e usa um Row para o layout.
                val tagsToShow = prof.tags.take(3)

                // 🌟 Ajuste de layout para as tags não empurrarem o "mais" para fora.
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Espaço reduzido
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tagsToShow.forEach { tag ->
                            AssistChip(
                                onClick = { /* Ação nula */ },
                                label = { Text(tag, fontSize = 11.sp) }, // 🌟 FONTE MENOR (11.sp)
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = PrimaryColor.copy(alpha = 0.1f),
                                    labelColor = PrimaryColor
                                ),
                                // 🌟 Redução do padding para encolher a tag visualmente
                                modifier = Modifier.height(26.dp)
                            )
                        }
                    }

                    // 🌟 TEXTO "+ N MAIS" EM LINHA SEPARADA OU COM MARGEM GARANTIDA
                    if (prof.tags.size > 3) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "+${prof.tags.size - 3} tags de serviço",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            // O uso do Column superior e esta linha separada garante que o texto não seja cortado
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
            }

            // 3. FOOTER (NOTA e BOTÃO AGENDAR)
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Nota: %.1f".format(prof.nota),
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Nota",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                }

                Button(
                    onClick = { navController.navigate("agendar/${prof.uid}") },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text("Agendar", color = Color.White)
                }
            }
        }
    }
}

// ------------------ FIRESTORE (COM FUNÇÃO ÚNICA DE FETCH) ------------------

suspend fun fetchProfissionaisSuspend(): List<Profissional> {
    return try {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("usuarios")
            .get()
            .await()

        val profissionaisEncontrados = snapshot.documents.mapNotNull { doc ->
            try {
                val prof = doc.toObject(Profissional::class.java)

                val uid = doc.id
                val nome = doc.getString("nome") ?: prof?.nome ?: ""
                val tipo = doc.getString("tipo") ?: prof?.tipo ?: ""
                if (tipo == "tutor" || tipo.isBlank()) return@mapNotNull null

                val fotoUrl = doc.getString("fotoUrl") ?: prof?.fotoUrl ?: ""
                val nota = doc.getDouble("nota") ?: prof?.nota ?: 0.0
                val latitude = doc.getDouble("latitude") ?: prof?.latitude ?: 0.0
                val longitude = doc.getDouble("longitude") ?: prof?.longitude ?: 0.0

                val cidade = doc.getString("cidade") ?: doc.getString("bairro") ?: doc.getString("endereco") ?: prof?.cidade ?: ""
                val endereco = doc.getString("endereco") ?: prof?.endereco ?: ""

                val especialidadesRaw = doc.get("especialidades")
                val especialidades: List<String> = when (especialidadesRaw) {
                    is List<*> -> especialidadesRaw.mapNotNull { it?.toString() }
                    is String -> listOf(especialidadesRaw)
                    else -> prof?.especialidades ?: emptyList()
                }

                val tagsRaw = doc.get("tags")
                val tags: List<String> = when (tagsRaw) {
                    is List<*> -> tagsRaw.mapNotNull { it?.toString() }
                    else -> prof?.tags ?: emptyList()
                }

                Profissional(
                    uid = uid,
                    nome = nome,
                    cidade = cidade,
                    endereco = endereco,
                    nota = nota,
                    fotoUrl = fotoUrl,
                    latitude = latitude,
                    longitude = longitude,
                    disponibilidade = doc.getString("disponibilidade") ?: prof?.disponibilidade ?: "",
                    tipo = tipo,
                    especialidades = especialidades,
                    tags = tags
                )
            } catch (e: Exception) {
                Log.e("Firestore", "Erro ao mapear doc ${doc.id}: ${e.message}", e)
                null
            }
        }

        profissionaisEncontrados
    } catch (e: Exception) {
        Log.e("Firestore", "Erro ao buscar profissionais: ${e.message}", e)
        emptyList()
    }
}

// ------------------ LÓGICA DE LOCALIZAÇÃO (COM FALLBACK DE TESTE) ------------------

fun loadProfessionalsBasedOnLocation(
    context: Context,
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    onResult: (Location?, List<Profissional>) -> Unit
) {
    if (androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onResult(null, emptyList())
        return
    }

    fusedLocationClient.lastLocation
        .addOnSuccessListener { loc ->
            val finalLocation: Location?

            if (loc == null) {
                Log.e("HomeScreen", "lastLocation null. USANDO LOCALIZAÇÃO FIXA DE TESTE (Jaguariúna).")
                finalLocation = Location("fixed").apply {
                    latitude = -22.6989
                    longitude = -47.0125
                }
            } else {
                finalLocation = loc
                Log.d("HomeScreen", "Localização obtida: Lat=${loc.latitude}, Lon=${loc.longitude}")
            }

            if (finalLocation != null) {
                (context as? ComponentActivity)?.lifecycleScope?.launch(Dispatchers.IO) {
                    try {
                        val allProfessionals = fetchProfissionaisSuspend()
                        val profissionaisPerto = allProfessionals.filter {
                            val distancia = calcularDistancia(
                                finalLocation.latitude,
                                finalLocation.longitude,
                                it.latitude,
                                it.longitude
                            )
                            distancia < 15000 // até 15 km
                        }
                        onResult(finalLocation, profissionaisPerto)
                        Log.d("HomeScreen", "Encontrados ${profissionaisPerto.size} profissionais perto.")
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Erro ao buscar e filtrar profissionais: ${e.message}", e)
                        onResult(finalLocation, emptyList())
                    }
                }
            } else {
                onResult(null, emptyList())
            }
        }
        .addOnFailureListener { e ->
            Log.e("HomeScreen", "Erro ao obter lastLocation", e)
            onResult(null, emptyList())
        }
}

// ------------------ UTIL ------------------

fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0]
}
