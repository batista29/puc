import com.example.petcare.network.*;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.net.*;
import java.util.*;

public class AceitadoraDeConexao extends Thread
    {
        private ServerSocket        pedido;
        private ArrayList<Parceiro> usuarios;
        private MongoCollection<Document> msgs;

    public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios, MongoCollection<Document> mensagens)throws Exception
        {
            if (porta==null)
                throw new Exception ("Porta ausente");
            if(mensagens==null)throw new Exception("Mensagens ausentes");
            try
            {
                this.pedido =
                        new ServerSocket (Integer.parseInt(porta));
            }
            catch (Exception  erro)
            {
                throw new Exception ("Porta invalida");
            }

            if (usuarios==null)
                throw new Exception ("Usuarios ausentes");
            this.msgs = mensagens;
            this.usuarios = usuarios;
        }

        public void run ()
        {
            for(;;)
            {
                Socket conexao=null;
                try
                {
                    conexao = this.pedido.accept();
                }
                catch (Exception erro)
                {
                    continue;
                }

                SupervisoraDeConexao supervisoraDeConexao=null;
                try
                {
                    supervisoraDeConexao =
                            new SupervisoraDeConexao(conexao, usuarios,this.msgs);
                }
                catch (Exception erro)
                {} // sei que passei parametros corretos para o construtor
                supervisoraDeConexao.start();
            }
        }
    }
