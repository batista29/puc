package com.example.petcare.screens

import androidx.compose.ui.platform.LocalContext   // Para obter o contexto da Activity
import android.content.Intent                      // Para criar o Intent e abrir outra Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.model.Profissional
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Se você tiver essas classes em outro arquivo, mantenha os imports:
import com.example.petcare.screens.HomeVetScreen // Exemplo de classe de destino
import com.example.petcare.screens.AgendaVetScreen // Exemplo de classe de destino
import com.example.petcare.screens.ProfessionalProfile // Exemplo de classe de destino

// ------------------ ACTIVITY ------------------
class EditProfessionalProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                ProfileScreen()
            }
        }
    }
}

// ---------- Repositório Atualizado ----------
object ProfissionalRepository {

    suspend fun getProfissionalAtual(): Profissional? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val db = FirebaseFirestore.getInstance()
        return try {
            val doc = db.collection("usuarios").document(userId).get().await()
            if (doc.exists()) doc.toObject(Profissional::class.java) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun atualizarProfissional(
        nome: String,
        cep: String,
        especialidades: List<String>,
        tags: List<String>,
        descricao: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        try {
            val updates = mapOf(
                "nome" to nome,
                "cep" to cep,
                "especialidades" to especialidades,
                "tags" to tags,
                "descricao" to descricao
            )
            db.collection("usuarios").document(userId).update(updates).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
// ---------- Componentes de UI ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val scope = rememberCoroutineScope()
    val PrimaryColor = Color(0xFF2A5C67)

    // Estados dos campos
    var nome by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    // Estados das Listas
    var especialidadesSelecionadas by remember { mutableStateOf(listOf<String>()) }
    var tagsSelecionadas by remember { mutableStateOf(listOf<String>()) }

    // Estados de controle
    var loading by remember { mutableStateOf(true) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var dropDownEspecialidadeAberto by remember { mutableStateOf(false) }

    // Dados estáticos para os menus
    val listaEspecialidades = listOf(
        "Clínico Geral", "Cirurgião", "Dermatologia", "Cardiologia", "Oftalmologia",
        "Ortopedia", "Odontologia", "Neurologia", "Oncologia", "Endocrinologia",
        "Nutrição", "Anestesia", "Gastroenterologia", "Comportamental (Etologia)", "Exóticos"
    )

    val listaTags = listOf(
        "Atende online", "Atende em domicílio", "Atende no consultório",
        "Aceita plano de saúde pet", "Urgência", "24h", "Finais de semana",
        "Atende cães", "Atende gatos", "Animais exóticos"
    )

    // --- CARREGAR DADOS ---
    LaunchedEffect(Unit) {
        val profissional = ProfissionalRepository.getProfissionalAtual()
        profissional?.let {
            nome = it.nome
            cep = it.cep
            especialidadesSelecionadas = it.especialidades
            tagsSelecionadas = it.tags
            descricao = it.descricao // Garante que a descrição também seja carregada
        }
        loading = false
    }

    Scaffold(
        bottomBar = { ProfileBottomMenu() },
        containerColor = Color.White // Fundo branco
    ) { innerPadding ->
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    // ⭐️ Adicionado Scroll para telas menores
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os campos
            ) {

                // ⭐️ Título da Tela
                Text(
                    text = "Editar Perfil Profissional",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo Nome (Estilo Login/Cadastro)
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = PrimaryColor,
                        unfocusedLabelColor = Color.Gray,
                    )
                )

                // Campo CEP (Estilo Login/Cadastro)
                OutlinedTextField(
                    value = cep,
                    onValueChange = { cep = it },
                    label = { Text("CEP (Local de Atendimento)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = PrimaryColor,
                        unfocusedLabelColor = Color.Gray,
                    )
                )

                // Campo da descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição Profissional/Sobre Você") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Altura maior para descrição
                    shape = RoundedCornerShape(6.dp),
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = PrimaryColor,
                        unfocusedLabelColor = Color.Gray,
                    )
                )

                // Dropdown de Especialidades
                ExposedDropdownMenuBox(
                    expanded = dropDownEspecialidadeAberto,
                    onExpandedChange = { dropDownEspecialidadeAberto = !dropDownEspecialidadeAberto },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = if (especialidadesSelecionadas.isEmpty()) ""
                        else especialidadesSelecionadas.joinToString(", "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Especialidades") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(dropDownEspecialidadeAberto) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = PrimaryColor,
                            unfocusedLabelColor = Color.Gray,
                        )
                    )

                    DropdownMenu(
                        expanded = dropDownEspecialidadeAberto,
                        onDismissRequest = { dropDownEspecialidadeAberto = false },
                        modifier = Modifier.fillMaxWidth(0.85f) // Largura do menu
                    ) {
                        listaEspecialidades.forEach { esp ->
                            val estaSelecionada = esp in especialidadesSelecionadas
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = estaSelecionada,
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(checkedColor = PrimaryColor)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(esp)
                                    }
                                },
                                onClick = {
                                    especialidadesSelecionadas = if (estaSelecionada)
                                        especialidadesSelecionadas - esp
                                    else
                                        especialidadesSelecionadas + esp
                                }
                            )
                        }
                    }
                }

                // Dropdown de Tags
                TagsDropDown(
                    listaTags = listaTags,
                    tagsSelecionadas = tagsSelecionadas,
                    onTagsChange = { tagsSelecionadas = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- SALVAR DADOS ---
                if (especialidadesSelecionadas.isNotEmpty() && tagsSelecionadas.isNotEmpty()
                    && nome.isNotEmpty() && cep.isNotEmpty() && descricao.isNotEmpty()){
                    Button(
                        onClick = {
                            scope.launch {
                                ProfissionalRepository.atualizarProfissional(
                                    nome,
                                    cep,
                                    especialidadesSelecionadas,
                                    tagsSelecionadas,
                                    descricao
                                )
                                mensagem = "Dados atualizados com sucesso!"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text("Salvar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    mensagem?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(it, color = PrimaryColor, fontWeight = FontWeight.SemiBold)
                    }
                }else{
                // Mensagem de feedback se estiver errado
                    mensagem = "Não deixe campos vazios"
                    mensagem?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(it, color = Color.Red, fontWeight = FontWeight.SemiBold)
                }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsDropDown(
    listaTags: List<String>,
    tagsSelecionadas: List<String>,
    onTagsChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val textoCampo = if (tagsSelecionadas.isEmpty()) "" else tagsSelecionadas.joinToString(", ")
    val PrimaryColor = Color(0xFF2A5C67)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = textoCampo,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tags (Serviços/Atendimento)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = PrimaryColor,
                unfocusedLabelColor = Color.Gray,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f) // Largura do menu
        ) {
            listaTags.forEach { tag ->
                val estaSelecionada = tag in tagsSelecionadas
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = estaSelecionada,
                                onCheckedChange = null,
                                colors = CheckboxDefaults.colors(checkedColor = PrimaryColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tag)
                        }
                    },
                    onClick = {
                        val novaLista = if (estaSelecionada) tagsSelecionadas - tag else tagsSelecionadas + tag
                        onTagsChange(novaLista)
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileBottomMenu() {
    val context = LocalContext.current
    val PrimaryColor = Color(0xFF2A5C67)

    NavigationBar(containerColor = Color.White) {
        val items = listOf(R.drawable.ic_home, R.drawable.ic_agenda, R.drawable.ic_perfil)
        // Assume que este é o ícone de perfil (índice 2)
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
                    selectedIconColor = PrimaryColor,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}