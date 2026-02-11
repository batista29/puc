package com.example.petcare.login_cadastrar

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
// CORREÇÃO: Usando a importação correta para KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.OutlinedTextFieldDefaults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.text.input.KeyboardCapitalization



// --------------------------------------------------------
// ACTIVITY PRINCIPAL
// --------------------------------------------------------
class CriarPerfilTutorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            PetCareTheme {
                CriarPerfilTutorScreen()
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
fun CriarPerfilTutorScreen() {
    val termosDeUso = """
        Os presentes Termos de Uso regulam a utilização do aplicativo de consultas veterinárias (“Aplicativo”). Ao acessar ou utilizar o Aplicativo, o usuário declara que leu, compreendeu e concorda integralmente com estes Termos, bem como com a Política de Privacidade aplicável...
    """.trimIndent()

    // -------------------- ESTADOS --------------------
    var mostrarDialogTermos by remember { mutableStateOf(false) }
    var termosLidosCompletamente by remember { mutableStateOf(false) }
    var showPasswordRequirements by remember { mutableStateOf(false) }

    var isSenhaVisivel by remember { mutableStateOf(false) }
    val visualTransformation = if (isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation()
    val icon = if (isSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var aceitoTermos by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val adaptiveContentColor = MaterialTheme.colorScheme.onSurface

    // -------------------- VALIDAÇÃO DE ERRO --------------------
    // NOVA VALIDAÇÃO DE NOME: Apenas letras e espaços
    val isNomeInvalido = remember(nome) { nome.isNotEmpty() && !validarApenasLetrasEspacos(nome) }
    val isTelefoneInvalido = remember(telefone) { telefone.isNotEmpty() && !validarApenasDigitos(telefone) }
    val isSenhaValida = remember(senha) { senha.isEmpty() || validarSenha(senha) }
    val showSenhaError = remember(senha) { senha.isNotEmpty() && !isSenhaValida }

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
            isError = isNomeInvalido,
            supportingText = {
                if (isNomeInvalido) {
                    Text("O nome deve conter apenas letras e espaços.", color = Color.Red)
                }
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(5.dp))

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(5.dp))

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
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = isTelefoneInvalido,
            supportingText = {
                if (isTelefoneInvalido) {
                    Text("O telefone deve conter apenas números.", color = Color.Red)
                }
            }
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

        Button(
            onClick = {
                val camposObrigatoriosPreenchidos = nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && telefone.isNotBlank()

                if (!camposObrigatoriosPreenchidos) {
                    Toast.makeText(context, "Preencha todos os campos obrigatórios!", Toast.LENGTH_LONG).show()
                } else if (isNomeInvalido) {
                    Toast.makeText(context, "Corrija o campo Nome (apenas letras e espaços).", Toast.LENGTH_LONG).show()
                } else if (isTelefoneInvalido) {
                    Toast.makeText(context, "Corrija o campo Telefone (apenas números).", Toast.LENGTH_LONG).show()
                } else if (!validarSenha(senha)) {
                    Toast.makeText(context, "A senha não cumpre todos os requisitos de segurança!", Toast.LENGTH_LONG).show()
                    showPasswordRequirements = true
                } else if (!aceitoTermos) {
                    Toast.makeText(context, "Você precisa ler e aceitar os Termos de Uso.", Toast.LENGTH_LONG).show()
                } else {
                    isLoading = true

                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val auth = FirebaseAuth.getInstance()
                            val db = FirebaseFirestore.getInstance()
                            val result = auth.createUserWithEmailAndPassword(email, senha).await()
                            val uid = result.user?.uid ?: return@launch

                            val dadosTutor = hashMapOf(
                                "uid" to uid,
                                "tipo" to "tutor",
                                "nome" to nome,
                                "email" to email,
                                "telefone" to telefone,
                                "dataCriacao" to System.currentTimeMillis()
                            )

                            db.collection("usuarios").document(uid).set(dadosTutor).await()

                            launch(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "Cadastro de Tutor concluído!", Toast.LENGTH_SHORT).show()
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