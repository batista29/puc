package com.example.supertasks

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
import com.example.supertasks.ui.theme.SuperTasksTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperTasksTheme {
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

fun addNewTestTask(){
    //referencia do singleton firestore.
    val db = Firebase.firestore

    val taskTestDoc = hashMapOf(
        "nome" to "Natã Batista Fernandes",
        "Idade" to 19
    )
    db.collection("tasks").add(taskTestDoc)

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Button(onClick = {
        addNewTestTask()
    }) {
        Text("Teste do firestore")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperTasksTheme {
        Greeting("Android")
    }
}