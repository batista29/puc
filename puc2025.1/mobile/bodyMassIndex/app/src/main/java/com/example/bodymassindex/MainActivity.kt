package com.example.bodymassindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bodymassindex.ui.theme.BodyMassIndexTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BodyMassIndexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun addNewBMI(){
    val db = Firebase.firestore

    val imcTestDoc = hashMapOf(
        "Nome" to "Sofia",
        "Cidade" to "Jaguariúna",
        "Estado" to "São Paulo"
    )

    db.collection("imc").add(imcTestDoc)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Button(onClick = {
        addNewBMI()
    }) {
        Text("Adicionar IMC")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BodyMassIndexTheme {
        Greeting("Android")
    }
}