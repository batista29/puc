package com.example.petcare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.R
import com.example.petcare.login_cadastrar.InitialScreenActivity
import com.example.petcare.ui.theme.GreenPrimary
import com.example.petcare.ui.theme.PetCareTheme
import com.example.petcare.ui.theme.White

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PetCare", "WelcomeActivity.onCreate()")

        setContent {
            PetCareTheme {
                WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    val context = LocalContext.current
    val sharedPref: SharedPreferences = context.getSharedPreferences("PetCarePrefs", Context.MODE_PRIVATE)
    var isFirstRun by remember { mutableStateOf(sharedPref.getBoolean("firstRun", true)) }

    // Se não for a primeira execução, pula direto
    if (!isFirstRun) {
        LaunchedEffect(Unit) {
            Log.d("PetCare", "Pulando para InitialScreenActivity")
            context.startActivity(Intent(context, InitialScreenActivity::class.java))
        }
        return
    }

    // Tela de boas-vindas
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.petcare_logo),
                contentDescription = "Logo PetCare",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto com degradê combinando com o logo
            Text(
                text = "Seja bem-vindo ao PetCare!",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2CA2A4), // azul esverdeado
                            Color(0xFF65C89B)  // verde claro
                        )
                    ),
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão com fonte maior
            Button(
                onClick = {
                    sharedPref.edit().putBoolean("firstRun", false).apply()
                    Toast.makeText(context, "Bem-vindo(a)!", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, InitialScreenActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Prosseguir",
                    color = White,
                    fontSize = 20.sp // tamanho da fonte aumentado
                )
            }
        }
    }
}
