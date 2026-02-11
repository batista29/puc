import com.example.petcare.network.*;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import com.mongodb.client.model.Sorts;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SupervisoraDeConexao extends Thread
{
    private String              resposta= String.valueOf(' ');
    private Parceiro usuario;
    private Socket conexao;
    private ArrayList<Parceiro> usuarios;
    private MongoCollection<Document> msgs;
    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, MongoCollection<Document> mensagens)throws Exception
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (usuarios==null)
            throw new Exception ("Usuarios ausentes");

        this.conexao  = conexao;
        this.usuarios = usuarios;
        this.msgs = mensagens;
    }

    public void run ()
    {
        ObjectOutputStream transmissor;
        try
        {
            transmissor =
                    new ObjectOutputStream(
                            this.conexao.getOutputStream());
        }
        catch (Exception erro)
        {
            return;
        }

        ObjectInputStream receptor=null;
        try
        {
            receptor=
                    new ObjectInputStream(
                            this.conexao.getInputStream());
        }
        catch (Exception err0)
        {
            try
            {
                transmissor.close();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread

            return;
        }

        try
        {
            this.usuario =
                    new Parceiro(this.conexao,receptor,transmissor);
        }
        catch (Exception erro)
        {} // sei que passei os parametros corretos

        try
        {
            synchronized (this.usuarios)
            {
                this.usuarios.add (this.usuario);
            }


            for(;;)
            {
                Comunicado comunicado = this.usuario.envie ();

                if(comunicado==null || this.msgs == null)
                    return;
                else if (comunicado instanceof PedidoDePerguntas) {
                    FindIterable<Document> perguntas = this.msgs.find().sort(Sorts.ascending("numero"));

                    StringBuilder perguntasParaCliente=new StringBuilder();
                    byte contagem = 0;
                    perguntasParaCliente.append("Selecione qual coisa deseja tirar duvida?\n");

                    for(Document doc : perguntas){
                        String pergunta = doc.get("numero").toString() + ":" + doc.get("pergunta").toString();
                        perguntasParaCliente.append(pergunta).append("\n");
                        contagem++;
                    }

                    perguntasParaCliente.append("Digite o numero que está na frente da pergunta ou digite 0 para sair");

                    this.usuario.receba(new Resultado(perguntasParaCliente.toString(),contagem));

                } else if (comunicado instanceof PedidoDeOperacao)
                {
                        PedidoDeOperacao pedidoDeOperacao = (PedidoDeOperacao)comunicado;

                        try {
                            Document doc = this.msgs.find(Filters.eq("numero",pedidoDeOperacao.getOperacao())).first();
                            if( doc != null){
                                this.resposta = doc.get("resposta").toString();
                            }else {
                                this.resposta = "Documento não Encontrado ";
                            }
                        }catch (Exception e) {
                            throw new Exception("Erro no banco " +  e.getMessage());
                        }


                        this.usuario.receba (new Resultado (this.resposta));
                }
                else if (comunicado instanceof PedidoParaSair)
                {
                    synchronized (this.usuarios)
                    {
                        this.usuarios.remove (this.usuario);
                    }
                    this.usuario.adeus();
                    return;
                }
            }
        }
        catch (Exception erro)
        {
            erro.printStackTrace();
            try
            {
                transmissor.close ();
                receptor   .close ();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread
        }
    }
}