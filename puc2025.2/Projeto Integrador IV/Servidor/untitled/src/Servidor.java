import com.example.petcare.network.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class Servidor
{
    public static String PORTA_PADRAO = "3000";

    public static void main (String[] args)
    {
        if (args.length>1)
        {
            System.err.println ("Uso esperado: java Servidor [PORTA]\n");
            return;
        }

        String porta=Servidor.PORTA_PADRAO;

        if (args.length==1)
            porta = args[0];


        MongoCollection<Document> mensagens = null;
        String url = "mongodb+srv://servidor:mansur12345@petcare.ia81fdf.mongodb.net/?appName=PetCare";
        MongoClient client = null;
        try {
            client = MongoClients.create(url);
            MongoDatabase database = client.getDatabase("Mensagens");
            mensagens=database.getCollection("Mensagens");
            System.out.println("Conexão com o banco feita");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ArrayList<Parceiro> usuarios =new ArrayList<Parceiro> ();

        AceitadoraDeConexao aceitadoraDeConexao=null;
        try
        {
            aceitadoraDeConexao =new AceitadoraDeConexao(porta, usuarios, mensagens);
            aceitadoraDeConexao.start();
        }
        catch (Exception erro)
        {
            System.err.println ("Escolha uma porta apropriada e liberada para uso!\n");
            return;
        }

        final MongoClient desativar = client;
        for(;;)
        {
            System.out.println ("O servidor esta ativo! Para desativa-lo,");
            System.out.println ("use o comando \"desativar\"\n");
            System.out.print   ("> ");

            String comando=null;
            try
            {
                comando = Teclado.getUmString();
            }
            catch (Exception erro)
            {}

            if (comando.toLowerCase().equals("desativar"))
            {
                synchronized (usuarios)
                {
                    ComunicadoDeDesligamento comunicadoDeDesligamento = new ComunicadoDeDesligamento ();

                    for (Parceiro usuario:usuarios)
                    {
                        try
                        {
                            usuario.receba (comunicadoDeDesligamento);
                            usuario.adeus  ();
                        }
                        catch (Exception _)
                        {}
                    }
                }

                try {
                    if(desativar!=null){
                        desativar.close();
                        //#TODO tirar essa linha dps
                        System.out.println("Servidor do Mongo fechado");
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao fechar o banco");
                }
                System.out.println ("O servidor foi desativado!\n");
                System.exit(0);
            }
            else
                System.err.println ("Comando invalido!\n");
        }
    }
}