package com.example.petcare.login_cadastrar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.MainActivity
import com.example.petcare.R
import com.example.petcare.ui.theme.PetCareTheme

class InitialScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetCareTheme {
                InitialScreen()
            }
        }
    }
}

@Composable
fun InitialScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.petcare_logo),
            contentDescription = "PetCare Logo",
            modifier = Modifier
                .size(170.dp)
                .padding(bottom = 18.dp)
        )

        Text(
            text = "PetCare",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E6B68)
        )

        Text(
            text = "A saúde do seu Pet\nsempre em primeiro lugar",
            fontSize = 18.sp,
            color = Color(0xFF6B6B6B),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Botão ENTRAR → abre TelaLoginActivity
        Button(
            onClick = {
                val intent = Intent(context, TelaLoginActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E6B68)),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "ENTRAR",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = { context.startActivity(Intent(context, CadastrarPerfilActivity::class.java)) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E6B68)),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "CADASTRAR",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}
