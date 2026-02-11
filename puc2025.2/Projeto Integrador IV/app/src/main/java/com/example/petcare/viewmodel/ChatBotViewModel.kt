package com.example.petcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.network.Comunicado
import com.example.petcare.network.Parceiro
import com.example.petcare.network.PedidoDeOperacao
import com.example.petcare.network.PedidoDePerguntas
import com.example.petcare.network.PedidoParaSair
import com.example.petcare.network.Resultado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket


data class ChatMessage(val message:String, val User: Boolean)


//Preciso fazer isso para eu conseguir botar o cliente no app
// já que o aplicativo não consegue rodar o operações de rede como conectar a um socket
class ChatBotViewModel: ViewModel() {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> =_chatHistory

    private val IP =""//Colocar o IP do computador aq :)
    private val Porta = 3000
    private var parceiro: Parceiro? = null

    private val _conectionStatus = MutableStateFlow("Desconectado")

    init {
        conexaoEPerguntas()
    }
    private var maxOpcao: Byte =0

    private fun conexaoEPerguntas(){
        //Executa o codigo de rede fora da thread principal
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Socket: IP do computador para rodar e a porta do servidor que o nosso é 3000 por padrão
                val socket = Socket(IP,Porta)
                //o tranmisor ele pega
                val mensageiro_para_o_Server = ObjectOutputStream(socket.getOutputStream())
                //para que os dados que enviar não ficar preso no caminho
                mensageiro_para_o_Server.flush()
                val mensagem_do_servidor = ObjectInputStream(socket.getInputStream())

                parceiro = Parceiro(socket,mensagem_do_servidor,mensageiro_para_o_Server)
                _conectionStatus.value = "Conectado"

                parceiro?.receba(PedidoDePerguntas())

                val perguntas = esperarResultado()
                if(perguntas!=null){
                    addMensagensNoHistorico(perguntas.valorResultante,false)
                    maxOpcao = perguntas.totalopcao.toByte()

                }else {
                    throw Exception("Não recebeu as Perguntas")
                }

            }catch (e: Exception){
                e.printStackTrace()
                _conectionStatus.value = "Erro de conexão"
                addMensagensNoHistorico("Erro ao conectar ao servidor:${e.message}",false)

            }
        }

    }

    fun mandarMensagem(msg: String){
        addMensagensNoHistorico(msg , true)

        val p = parceiro
        if (p==null){
            addMensagensNoHistorico("Não conectado. Tentando Reconectar...",false)
            conexaoEPerguntas()
            return
        }

        val opcao = msg.toByteOrNull()

        if(opcao == null || opcao<0 || opcao>maxOpcao){
            addMensagensNoHistorico("Opcão inválida. Digite um Numero entre 0 e $maxOpcao",false)
            return
        }
        if (opcao == 0.toByte()){
            sendExitRequest(p)
        }else{
            sendOperationRequest(p, opcao)
        }
    }

    private fun sendExitRequest(parceiroPassando: Parceiro) {
        val p = parceiroPassando
        if (p==null)return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                p?.receba(PedidoParaSair())
                p?.adeus()

            }catch (e: Exception){

            }finally {
                parceiro = null
                _conectionStatus.value = "Desconectado"
                addMensagensNoHistorico("Obrigado! Conexão encerrada.", false)
            }
        }
    }

    private fun sendOperationRequest(parceiroPassando: Parceiro, opcao: Byte) {
        val p = parceiroPassando
        if (p==null){
            addMensagensNoHistorico("Não conectado. Tentando Reconectar...",false)
            conexaoEPerguntas()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                p.receba(PedidoDeOperacao(opcao))
                val resultado = esperarResultado()
                if (resultado !=null)
                    addMensagensNoHistorico(resultado.valorResultante,false)
            }catch (e: Exception){
                e.printStackTrace()
                addMensagensNoHistorico("Erro de comunicação: ${e.message}",false)
            }
        }
    }


    private fun addMensagensNoHistorico(msg: String, user: Boolean){
        _chatHistory.value = _chatHistory.value + ChatMessage(msg,user)
    }
    private suspend fun esperarResultado(): Resultado?{
        return withContext(Dispatchers.IO){
            var com: Comunicado? =null
            try {
                while (com !is Resultado){
                    com = parceiro?.espie()
                }
                parceiro?.envie() as Resultado
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }

    }

    override fun onCleared() {
        parceiro?.let { sendExitRequest(it) }
        super.onCleared()
    }
}