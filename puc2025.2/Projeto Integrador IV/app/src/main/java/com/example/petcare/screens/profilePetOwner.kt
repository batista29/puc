package com.example.petcare.screens

// ------------------ IMPORTS ------------------
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp

import androidx.compose.material3.AlertDialog // 🌟 IMPORT ADICIONADO AQUI
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.petcare.R
import com.example.petcare.model.Tutor // Assumindo o modelo Tutor
import com.example.petcare.login_cadastrar.TelaLoginActivity // Assumindo import
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

// ------------------ CONSTANTS ------------------
private val PrimaryColor = Color(0xFF2A5C67)

// ------------------ 🌟 PROFILE SCREEN CONTENT ------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val activity = LocalContext.current as? Activity

    var tutor by remember { mutableStateOf<Tutor?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    val tipoUsuario = "Tutor"
    val scope = rememberCoroutineScope()

    // --- Launcher para Base64 ---
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null && activity != null) {
                scope.launch {
                    val base64String = uriToBase64(activity, uri)

                    // Verificação de tamanho
                    if (base64String.length > 900000) {
                        Toast.makeText(context, "Imagem muito grande. Limite de 1MB excedido.", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    if (base64String.isNotEmpty()) {
                        saveBase64ToFirestore(base64 = base64String, uid = uid, db = db,
                            onSuccess = { tutor = tutor?.copy(fotoUrl = base64String); Toast.makeText(context, "Foto atualizada (Base64)!", Toast.LENGTH_SHORT).show() },
                            onError = { e -> Toast.makeText(context, "Falha ao salvar Base64: ${e.message}", Toast.LENGTH_LONG).show() }
                        )
                    }
                }
            }
        }
    )

    // --- Carregamento de Dados ---
    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val doc = db.collection("usuarios").document(uid).get().await()
                tutor = doc.toObject(Tutor::class.java)
            } catch (_: Exception) { /* ... */ }
            isLoading = false
        } else { isLoading = false }
    }

    // --- Logout ---
    val onLogoutAction: () -> Unit = {
        auth.signOut()
        val intent = Intent(context, TelaLoginActivity ::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        (context as? Activity)?.finish()
    }

    // --- CONTEÚDO (Começa com TopAppBar) ---
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // AQUI VOCÊ PODE AUMENTAR À VONTADE 🤩
        Image(
            painter = painterResource(id = R.drawable.petcare_logo),
            contentDescription = "Logo do PetCare",
            modifier = Modifier.size(110.dp), // <<< AUMENTE O TAMANHO AQUI
            contentScale = ContentScale.Fit
        )

        Text(
            "Meu Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryColor
        )

    // Resto do conteúdo da tela
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (tutor != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {

                    // --- Seção da Foto de Perfil (com Ícone de Lápis) ---
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        // Lógica de exibição da foto Base64
                        val imageModifier = Modifier.fillMaxSize().clip(CircleShape)
                        val bitmap = tutor!!.fotoUrl?.let { base64ToBitmap(it) }
                        if (bitmap != null) {
                            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Foto do perfil", contentScale = ContentScale.Crop, modifier = imageModifier)
                        } else {
                            Image(painter = painterResource(id = R.drawable.ic_perfil), contentDescription = "Placeholder", contentScale = ContentScale.Crop, modifier = imageModifier)
                        }

                        // 🌟 Ícone de Lápis (Edit)
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Adicionar Foto",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 8.dp, y = 8.dp)
                                .clip(CircleShape)
                                .background(PrimaryColor)
                                .padding(4.dp)
                                .size(24.dp)
                        )
                    }

                    // --- Card com Informações Principais ---
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Nome com botão de edição
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = tutor!!.nome,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface)
                                IconButton(onClick = { newName = tutor!!.nome; showEditNameDialog = true }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar Nome", tint = PrimaryColor)
                                }
                            }

                            // Tipo de Usuário
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clip(RoundedCornerShape(8.dp))
                                    .background(PrimaryColor.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = tipoUsuario, style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface)
                            }

                            Divider(Modifier.padding(vertical = 4.dp))

                            // E-mail
                            Text(text = tutor!!.email, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Botão Sair
                    OutlinedButton(
                        onClick = onLogoutAction,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sair", style = MaterialTheme.typography.titleMedium)
                    }
                }
            } else {
                Text("Usuário não encontrado.", modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    // Diálogo de Edição de Nome
    if (showEditNameDialog) {
        EditNameDialog(
            currentName = newName,
            onDismiss = { showEditNameDialog = false },
            onConfirm = { name -> scope.launch { updateUserName(uid, db, name); tutor = tutor?.copy(nome = name); showEditNameDialog = false } }
        )
    }
}

// ------------------ FUNÇÕES AUXILIARES ------------------

@Composable

fun EditNameDialog(currentName: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf(currentName) }

    val isTextValid = text.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nome") },

        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter {
                        it.isLetter() || it.isWhitespace()
                    }
                    text = filteredValue
                },
                label = { Text("Novo Nome") },
                singleLine = true
            )
        },

        confirmButton = {
            Button(
                onClick = { onConfirm(text) },
                enabled = isTextValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2A5C67),
                    contentColor = Color.White
                )
            ) {
                Text("Salvar")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF2A5C67)
                )
            ) {
                Text("Cancelar")
            }
        }
    )
}


fun uriToBase64(activity: Activity, uri: Uri): String {

    return try {

        val inputStream = activity.contentResolver.openInputStream(uri)

        val bitmap = BitmapFactory.decodeStream(inputStream)



// Otimização: Compressão para 60% da qualidade JPEG

        val COMPRESSION_QUALITY = 60



        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayOutputStream)



        Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

    } catch (e: Exception) {

        e.printStackTrace()

        ""

    }

}



fun base64ToBitmap(base64String: String): android.graphics.Bitmap? {

    return try {

        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    } catch (e: Exception) { e.printStackTrace(); null }

}



suspend fun saveBase64ToFirestore(base64: String, uid: String?, db: FirebaseFirestore, onSuccess: () -> Unit, onError: (Exception) -> Unit) {

    if (uid == null) { onError(Exception("User not authenticated")); return }

    try { db.collection("usuarios").document(uid).update("fotoUrl", base64).await(); onSuccess() }

    catch (e: Exception) { onError(e) }

}



suspend fun updateUserName(uid: String?, db: FirebaseFirestore, newName: String) {
    if (uid != null) { db.collection("usuarios").document(uid).update("nome", newName).await() }
}