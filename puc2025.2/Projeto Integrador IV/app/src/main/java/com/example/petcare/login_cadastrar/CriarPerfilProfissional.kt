package com.example.petcare.login_cadastrar

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.ui.theme.PetCareTheme
import com.example.petcare.util.CepGeocodingService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.KeyboardCapitalization


// --------------------------------------------------------
// ACTIVITY PRINCIPAL
// --------------------------------------------------------
class CriarPerfilProfissionalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            PetCareTheme {
                CriarPerfilProfissionalScreen()
            }
        }
    }
}

// --------------------------------------------------------
// CONSTANTES E CORES
// --------------------------------------------------------
private val PrimaryColor = Color(0xFF2E6B68)

// --------------------------------------------------------
// TELA COMPOSABLE
// --------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarPerfilProfissionalScreen() {
    val termosDeUso = """
        Os presentes Termos de Uso regulam a utilização do aplicativo de consultas veterinárias (“Aplicativo”). Ao acessar ou utilizar o Aplicativo, o usuário declara que leu, compreendeu e concorda integralmente com estes Termos, bem como com a Política de Privacidade aplicável...
    """.trimIndent()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val adaptiveContentColor = MaterialTheme.colorScheme.onSurface

    // -------------------- ESTADOS --------------------
    var mostrarDialogTermos by remember { mutableStateOf(false) }
    var termosLidosCompletamente by remember { mutableStateOf(false) }
    var showPasswordRequirements by remember { mutableStateOf(false) }

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var aceitoTermos by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var isSenhaVisivel by remember { mutableStateOf(false) }
    val visualTransformation = if (isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation()
    val icon = if (isSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    // -------------------- VALIDAÇÕES --------------------
    val isNomeInvalido = remember(nome) { nome.isNotEmpty() && !validarApenasLetrasEspacos(nome) }
    val isTelefoneInvalido = remember(telefone) { telefone.isNotEmpty() && !validarApenasDigitos(telefone) }
    val isSenhaValida = remember(senha) { senha.isEmpty() || validarSenha(senha) }
    val showSenhaError = remember(senha) { senha.isNotEmpty() && !isSenhaValida }

    // -------------------- LISTAS / ESTILOS --------------------
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
    var especialidadesSelecionadas by remember { mutableStateOf(listOf<String>()) }
    var dropEspecialidadeAberto by remember { mutableStateOf(false) }
    var tagsSelecionadas by remember { mutableStateOf(listOf<String>()) }
    var dropTagsAberto by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        focusedBorderColor = PrimaryColor,
        unfocusedBorderColor = PrimaryColor.copy(alpha = 0.5f),
        focusedLabelColor = PrimaryColor,
        unfocusedLabelColor = PrimaryColor,
        cursorColor = PrimaryColor,
        focusedTextColor = adaptiveContentColor,
        unfocusedTextColor = adaptiveContentColor
    )

    // -------------------- DIÁLOGO TERMOS DE USO --------------------
    if (mostrarDialogTermos) {
        AlertDialog(
            onDismissRequest = { mostrarDialogTermos = false },
            title = { Text(text = "Termos de Uso e Política de Privacidade", fontWeight = FontWeight.Bold, color = PrimaryColor) },
            text = {
                val scrollState = rememberScrollState()

                LaunchedEffect(scrollState.maxValue) {
                    snapshotFlow { scrollState.value == scrollState.maxValue }
                        .collect { rolouAteOFim ->
                            if (rolouAteOFim && !termosLidosCompletamente) {
                                termosLidosCompletamente = true
                                Toast.makeText(context, "Termos lidos. Você já pode aceitar.", Toast.LENGTH_LONG).show()
                            }
                        }
                }

                Column {
                    Text(
                        text = termosDeUso,
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp).verticalScroll(scrollState),
                        fontSize = 14.sp,
                        color = adaptiveContentColor
                    )
                    Spacer(modifier = Modifier.height(11.dp))
                    if (!termosLidosCompletamente) {
                        Text(
                            text = "Role até o final para habilitar a aceitação.",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogTermos = false }) {
                    Text("Fechar", color = PrimaryColor)
                }
            }
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(Modifier.weight(1f))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.petcare_logo),
            contentDescription = "PetCare Logo",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "PetCare",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(11.dp))

        // NOME COMPLETO
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            isError = isNomeInvalido, // Exibe erro se o nome for inválido
            supportingText = {
                if (isNomeInvalido) {
                    Text("O nome deve conter apenas letras e espaços.", color = Color.Red)
                }
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )

        OutlinedTextField(
            value = telefone,
            onValueChange = { newValue ->
                val filteredValue = newValue.filter { it.isDigit() }
                if (filteredValue.length <= 11) {
                    telefone = filteredValue
                }
            },
            label = { Text("Telefone (WhatsApp)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = telefone.isNotEmpty() && !validarApenasDigitos(telefone),
            supportingText = {
                if (telefone.isNotEmpty() && !validarApenasDigitos(telefone)) {
                    Text("O telefone deve ter entre 8 e 11 dígitos e conter apenas números.", color = Color.Red)
                }
            }
        )

        // EMAIL PROFISSIONAL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email profissional") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(5.dp))

        // SENHA - COM VALIDAÇÃO E ÍCONE INFO
        OutlinedTextField(
            value = senha,
            colors = textFieldColors,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(12.dp),
            isError = showSenhaError,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { isSenhaVisivel = !isSenhaVisivel }) {
                        Icon(imageVector = icon, contentDescription = if (isSenhaVisivel) "Esconder senha" else "Mostrar senha", tint = PrimaryColor)
                    }
                    IconButton(onClick = { showPasswordRequirements = true }) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "Requisitos de Senha", tint = PrimaryColor)
                    }
                }
            },
            supportingText = {
                if (showSenhaError) {
                    Text("A senha não cumpre os requisitos mínimos.", color = Color.Red)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(5.dp))

        // -------------------- DROPDOWN ESPECIALIDADES --------------------
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Especialidades Veterinárias",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = adaptiveContentColor
            )
            OutlinedTextField(
                value = especialidadesSelecionadas.joinToString(", "),
                onValueChange = {},
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                readOnly = true,
                label = { Text("Selecione as especialidades") },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { dropEspecialidadeAberto = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Abrir", tint = PrimaryColor)
                    }
                }
            )
            DropdownMenu(
                expanded = dropEspecialidadeAberto,
                onDismissRequest = { dropEspecialidadeAberto = false }
            ) {
                listaEspecialidades.forEach { esp ->
                    val estaSelecionada = esp in especialidadesSelecionadas
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = estaSelecionada, onCheckedChange = { isChecked ->
                                    especialidadesSelecionadas =
                                        if (isChecked) especialidadesSelecionadas + esp
                                        else especialidadesSelecionadas - esp
                                }, colors = CheckboxDefaults.colors(checkedColor = PrimaryColor))
                                Text(esp)
                            }
                        },
                        onClick = {
                            especialidadesSelecionadas =
                                if (estaSelecionada) especialidadesSelecionadas - esp
                                else especialidadesSelecionadas + esp
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // -------------------- DROPDOWN TAGS --------------------
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Tags do profissional",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = adaptiveContentColor
            )
            OutlinedTextField(
                value = tagsSelecionadas.joinToString(", "),
                onValueChange = {},
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                readOnly = true,
                label = { Text("Selecione as tags") },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { dropTagsAberto = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Abrir", tint = PrimaryColor)
                    }
                }
            )
            DropdownMenu(
                expanded = dropTagsAberto,
                onDismissRequest = { dropTagsAberto = false }
            ) {
                listaTags.forEach { tag ->
                    val estaSelecionada = tag in tagsSelecionadas
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = estaSelecionada, onCheckedChange = { isChecked ->
                                    tagsSelecionadas =
                                        if (isChecked) tagsSelecionadas + tag
                                        else tagsSelecionadas - tag
                                }, colors = CheckboxDefaults.colors(checkedColor = PrimaryColor))
                                Text(tag)
                            }
                        },
                        onClick = {
                            tagsSelecionadas =
                                if (estaSelecionada) tagsSelecionadas - tag
                                else tagsSelecionadas + tag
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(11.dp))

        // -------------------- ENDEREÇO --------------------
        OutlinedTextField(
            value = endereco,
            onValueChange = { endereco = it },
            label = { Text("Endereço de atendimento") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = bairro,
                onValueChange = { bairro = it },
                label = { Text("Bairro") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = cep,
                onValueChange = { newValue ->
                    // Restringe o CEP para apenas 8 dígitos
                    val filteredValue = newValue.filter { it.isDigit() }
                    if (filteredValue.length <= 8) {
                        cep = filteredValue
                    }
                },
                label = { Text("CEP") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = textFieldColors,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(11.dp))

        // -------------------- TERMOS --------------------
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = aceitoTermos,
                onCheckedChange = { aceitoTermos = it },
                colors = CheckboxDefaults.colors(checkedColor = PrimaryColor)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { mostrarDialogTermos = true },
                ) {
                    Text(
                        text = "Ler",
                        color = PrimaryColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    )
                }
                Text(
                    text = "- Li e aceito os Termos de uso",
                    color = adaptiveContentColor,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // -------------------- BOTÃO CADASTRAR --------------------
        Button(
            onClick = {
                val camposObrigatoriosPreenchidos = nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && telefone.isNotBlank() && especialidadesSelecionadas.isNotEmpty() && cep.isNotBlank() && endereco.isNotBlank() && bairro.isNotBlank()

                if (!camposObrigatoriosPreenchidos) {
                    Toast.makeText(context, "Preencha todos os campos obrigatórios (incluindo Endereço, Bairro e CEP)!", Toast.LENGTH_LONG).show()
                } else if (isNomeInvalido) {
                    Toast.makeText(context, "Corrija o campo Nome (apenas letras e espaços).", Toast.LENGTH_LONG).show()
                } else if (isTelefoneInvalido) {
                    // Mensagem de erro consistente com a validação
                    Toast.makeText(context, "Corrija o campo Telefone (8 a 11 dígitos numéricos).", Toast.LENGTH_LONG).show()
                } else if (!validarSenha(senha)) {
                    Toast.makeText(context, "A senha não cumpre todos os requisitos de segurança!", Toast.LENGTH_LONG).show()
                    showPasswordRequirements = true
                } else if (!aceitoTermos) {
                    Toast.makeText(context, "Você precisa ler e aceitar os Termos de Uso.", Toast.LENGTH_LONG).show()
                } else {
                    isLoading = true

                    coroutineScope.launch(Dispatchers.IO) {
                        val auth = FirebaseAuth.getInstance()
                        val db = FirebaseFirestore.getInstance()

                        // LAT/LON
                        val coords = CepGeocodingService.getCoordsFromCep(context, cep)
                        val latitude = coords.latitude
                        val longitude = coords.longitude

                        if (latitude == 0.0 && longitude == 0.0) {
                            launch(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "CEP não encontrado ou inválido!", Toast.LENGTH_LONG).show()
                            }
                            return@launch
                        }

                        try {
                            val result = auth.createUserWithEmailAndPassword(email, senha).await()
                            val uid = result.user?.uid ?: return@launch

                            // Simulação de URL de foto
                            val fallbackImages = listOf(R.drawable.pet1, R.drawable.pet2, R.drawable.pet3)
                            val drawableRandom = fallbackImages.random()
                            val fotoUri = "android.resource://${context.packageName}/$drawableRandom"

                            val dadosProfissional = hashMapOf(
                                "uid" to uid,
                                "tipo" to "profissional",
                                "nome" to nome,
                                "email" to email,
                                "telefone" to telefone,
                                "especialidades" to especialidadesSelecionadas,
                                "tags" to tagsSelecionadas,
                                "endereco" to endereco,
                                "bairro" to bairro,
                                "cep" to cep,
                                "dataCriacao" to System.currentTimeMillis(),
                                "nota" to 0.0,
                                "fotoUrl" to fotoUri,
                                "latitude" to latitude,
                                "longitude" to longitude
                            )

                            db.collection("usuarios").document(uid).set(dadosProfissional).await()

                            launch(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "Cadastro concluído!", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, TelaLoginActivity::class.java))
                                (context as? ComponentActivity)?.finish()
                            }

                        } catch (e: Exception) {
                            launch(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Cadastrar", color = Color.White, fontSize = 18.sp)
            }
        }


        Spacer(Modifier.weight(1f))
    }

    // -------------------- DIÁLOGO REQUISITOS DE SENHA --------------------
    if (showPasswordRequirements) {
        PasswordRequirementsDialog(onDismiss = { showPasswordRequirements = false })
    }
}


// =======================================================
// FUNÇÕES AUXILIARES DE VALIDAÇÃO (ÚNICA FONTE)
// =======================================================

/**
 * Valida se a senha atende aos requisitos mínimos:
 * - Mínimo 8 caracteres, maiúscula, minúscula, número, especial.
 */
fun validarSenha(senha: String): Boolean {
    val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    return senha.matches(regex)
}

/**
 * Verifica se a string contém apenas dígitos e se possui um tamanho mínimo/máximo razoável para telefone (8 a 11 dígitos).
 */
fun validarApenasDigitos(texto: String): Boolean {
    // Permite string vazia para o estado inicial
    if (texto.isEmpty()) return true

    // Checa se contém apenas dígitos
    val apenasDigitos = texto.matches(Regex("\\d+"))

    // Checa se o tamanho é válido (mínimo de 8, máximo de 11)
    val tamanhoValido = texto.length >= 8 && texto.length <= 11

    return apenasDigitos && tamanhoValido
}

/**
 * Verifica se a string contém apenas letras e espaços.
 */
fun validarApenasLetrasEspacos(texto: String): Boolean {
    // Permite letras, espaços e acentos comuns. Permite string vazia.
    return texto.matches(Regex("^[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]*$"))
}




@Composable
fun Requisito(texto: String) {
    Text(texto, fontSize = 14.sp)
}