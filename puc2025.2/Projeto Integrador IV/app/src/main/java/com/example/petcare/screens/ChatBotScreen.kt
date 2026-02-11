package com.example.petcare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petcare.viewmodel.ChatBotViewModel
import com.example.petcare.viewmodel.ChatMessage
import kotlinx.coroutines.launch

@Composable
fun ChatbotScreen(viewModel: ChatBotViewModel = viewModel()) {
    var userInput by remember { mutableStateOf("") }

    val conversa by viewModel.chatHistory.collectAsState()
    //serve para controlar o cursor da lista(conversa)
    val statusConversa = rememberLazyListState()
    val scrol = rememberCoroutineScope()


    //caso chegue uma mensagem nova ele rola pra baixo
    LaunchedEffect(conversa)
    {
        if (conversa.isNotEmpty()){
            scrol.launch {
                statusConversa.animateScrollToItem(conversa.size -1)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Ícone do Chatbot
        AsyncImage(
            model = "https://cdn-icons-png.flaticon.com/512/4712/4712109.png",
            contentDescription = "Chatbot Icon",
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8E8E8))
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Nome do Chatbot
        Text(
            text = "Chatbot",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensagem do Chatbot
        LazyColumn(
            state = statusConversa,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            items(conversa){
                conversa ->
                ChatMessageItem(conversa)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Campo de texto do usuário
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFF345B63), RoundedCornerShape(6.dp)),
                placeholder = { Text("Digite sua mensagem...") },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if(userInput.isNotEmpty()){
                        viewModel.mandarMensagem(userInput)
                        userInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF345B63)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(text = "Enviar", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}


@Composable
fun ChatMessageItem(conversa: ChatMessage) {
    val alignment = if(conversa.User) Alignment.CenterEnd
    else Alignment.CenterStart
    val CordeFundo = if (conversa.User){
        Color(0xFFD0E8F2)
    }else Color(0xFFFFF9F9)

    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(1.dp, Color(0xFF345B63), RoundedCornerShape(8.dp))
                .background(CordeFundo,RoundedCornerShape(8.dp))
                .padding(14.dp)
        ){
            Text(
                text = conversa.message,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
    }


}



@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview() {
    ChatbotScreen()
}
