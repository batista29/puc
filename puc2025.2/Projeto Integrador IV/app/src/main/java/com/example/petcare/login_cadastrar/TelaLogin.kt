package com.example.petcare.login_cadastrar

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.screens.HomePetOwnerScreen
import com.example.petcare.screens.HomeVetScreen
import com.example.petcare.ui.theme.PetCareTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.OutlinedTextFieldDefaults

class TelaLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. CONFIGURAÇÃO DA ACTIVITY PARA AJUSTAR O TECLADO
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            PetCareTheme {
                TelaLoginScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLoginScreen() {
    val PrimaryColor = Color(0xFF2E6B68)

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPasswordRequirements by remember { mutableStateOf(false) }

    var isSenhaVisivel by remember { mutableStateOf(false) }
    val visualTransformation = if (isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation()
    val icon = if (isSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    // ESTADO DE ROLAGEM
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

        Image(
            painter = painterResource(id = R.drawable.petcare_logo),
            contentDescription = "PetCare Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 8.dp)
        )

        Text(
            text = "PetCare",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("nome@exemplo.com") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email",
                    tint = PrimaryColor
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = PrimaryColor.copy(alpha = 0.5f),
                focusedLabelColor = PrimaryColor,
                unfocusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // SENHA
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = PrimaryColor.copy(alpha = 0.5f),
                focusedLabelColor = PrimaryColor,
                unfocusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            ),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { isSenhaVisivel = !isSenhaVisivel }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (isSenhaVisivel) "Esconder senha" else "Mostrar senha",
                            tint = PrimaryColor
                        )
                    }
                    IconButton(onClick = { showPasswordRequirements = true }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Requisitos de Senha",
                            tint = PrimaryColor
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÃO ENTRAR
        Button(
            onClick = {
                if (email.isNotEmpty() && senha.isNotEmpty()) {
                    isLoading = true

                    val auth = FirebaseAuth.getInstance()
                    val db = FirebaseFirestore.getInstance()

                    auth.signInWithEmailAndPassword(email, senha)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid

                            if (uid != null) {
                                db.collection("usuarios").document(uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        isLoading = false
                                        if (document.exists()) {
                                            val tipo = document.getString("tipo")
                                            val nextIntent = when (tipo) {
                                                "tutor" -> Intent(context, HomePetOwnerScreen::class.java)
                                                "profissional" -> Intent(context, HomeVetScreen::class.java)
                                                else -> {
                                                    Toast.makeText(context, "Tipo de usuário não identificado", Toast.LENGTH_SHORT).show()
                                                    null
                                                }
                                            }

                                            // ⭐️ APLICAÇÃO DA CORREÇÃO AQUI ⭐️
                                            val finalIntent = nextIntent?.apply {
                                                // Estas flags limpam a pilha de atividades, impedindo o retorno ao Login
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            }

                                            if (finalIntent != null) {
                                                Toast.makeText(context, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                                                context.startActivity(finalIntent)
                                                // Finalizar a Activity de Login
                                                (context as? ComponentActivity)?.finish()
                                            }
                                        } else {
                                            Toast.makeText(context, "Usuário não encontrado no banco", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        Toast.makeText(context, "Erro ao buscar dados: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                isLoading = false
                                Toast.makeText(context, "Erro: UID não encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            isLoading = false
                            val errorMessage = when (it.message) {
                                null -> "Falha no login. Verifique as credenciais."
                                else -> "Falha no login: ${it.message}"
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }

                } else {
                    Toast.makeText(context, "Preencha o email e a senha", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Entrar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BOTÃO CADASTRO
        TextButton(
            onClick = {
                val intent = Intent(context, CadastrarPerfilActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(
                text = "Não tem conta? Cadastre-se",
                color = PrimaryColor,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.weight(1f))
    }

    // Diálogo de Requisitos de Senha
    if (showPasswordRequirements) {
        PasswordRequirementsDialog(onDismiss = { showPasswordRequirements = false })
    }
}
private val PrimaryColor = Color(0xFF2E6B68)
// ------------------ DIÁLOGO DE REQUISITOS DE SENHA ------------------
@Composable
fun PasswordRequirementsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Requisitos de Senha", fontWeight = FontWeight.Bold, color = PrimaryColor) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Sua senha deve conter:", fontSize = 15.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Requisito(texto = "• Mínimo de 8 caracteres.")
                Requisito(texto = "• Pelo menos 1 letra maiúscula (A-Z).")
                Requisito(texto = "• Pelo menos 1 letra minúscula (a-z).")
                Requisito(texto = "• Pelo menos 1 número (0-9).")
                Requisito(texto = "• Pelo menos 1 caractere especial (@$!%*?&).")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)) {
                Text("Entendi")
            }
        }
    )

}