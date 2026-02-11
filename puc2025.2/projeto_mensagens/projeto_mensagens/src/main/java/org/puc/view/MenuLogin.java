package org.puc.view;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.puc.database.MongoHandler;
import org.puc.message.entity.MessageEntity;
import org.puc.user.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.puc.database.EncryptionHandler.decrypt;
import static org.puc.database.EncryptionHandler.encrypt;

public class MenuLogin {
    private final Scanner sc = new Scanner(System.in);


    public void exibir(UserEntity user) throws Exception {
        int opcao;
        do {
            System.out.println("\n==============================");
            System.out.println("       BEM-VINDO, " + user.getNome() + "!");
            System.out.println("==============================");
            System.out.println("1. Ver mensagens NÃO lidas");
            System.out.println("2. Ver mensagens JÁ lidas");
            System.out.println("3. Ver mensagens ENVIADAS");
            System.out.println("4. Enviar mensagem");
            System.out.println("------------------------------");
            System.out.println("0. Sair");
            System.out.println("==============================");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarInbox(user, "nao_lida", true);
                case 2 -> listarInbox(user, "lida", false);
                case 3 -> listarEnviadas(user);
                case 4 -> enviarMensagem(user);
                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void listarInbox(UserEntity user, String status, boolean marcarAoAbrir) {
        try {
            Document filter = new Document("receptor", user.getUsername())
                    .append("status", status);
            List<Document> messages = MongoHandler.findDocuments("Mensagens", filter);

            String heading = status.equals("nao_lida") ? "Mensagens não lidas" : "Mensagens já lidas";
            if (messages.isEmpty()) {
                System.out.println(heading + ": nenhuma.");
                return;
            }

            System.out.print("Escolha o número da mensagem desejada: ");
            System.out.println("\n" + heading + ": " + messages.size());
            for (int i = 0; i < messages.size(); i++) {
                Document msg = messages.get(i);
                System.out.printf("%d - De: %s | Data: %s%n",
                        i, msg.getString("transmissor"), msg.getDate("dataEnvio"));
            }

            Integer idx = promptIndex(messages.size());
            if (idx == null) return;

            Document selecionada = messages.get(idx);
            String secretKey = promptSecret();
            try {
                String mensagem = decrypt(selecionada.getString("texto"), secretKey);
                exibirMensagemDetalhe(selecionada, mensagem, false);

                if (marcarAoAbrir) {
                    ObjectId id = selecionada.getObjectId("_id");
                    if (id != null) MessageEntity.marcarComoLida(id.toHexString());
                }
            } catch (Exception e) {
                System.out.println("Chave incorreta ou mensagem inválida.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar mensagens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarEnviadas(UserEntity user) {
        try {
            Document filter = new Document("transmissor", user.getUsername());
            List<Document> sent = MongoHandler.findDocuments("Mensagens", filter);

            if (sent.isEmpty()) {
                System.out.println("Você ainda não enviou mensagens.");
                return;
            }

            System.out.println("\nMensagens enviadas: " + sent.size());
            for (int i = 0; i < sent.size(); i++) {
                Document msg = sent.get(i);
                System.out.printf("%d - Para: %s | Data: %s%n",
                        i, msg.getString("receptor"), msg.getDate("dataEnvio"));
            }

            Integer idx = promptIndex(sent.size());
            if (idx == null) return;

            Document selecionada = sent.get(idx);
            String secretKey = promptSecret();
            try {
                String mensagem = decrypt(selecionada.getString("texto"), secretKey);
                exibirMensagemDetalhe(selecionada, mensagem, true);
            } catch (Exception e) {
                System.out.println("Chave incorreta para essa mensagem enviada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar mensagens enviadas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarMensagem(UserEntity user) throws Exception {
        System.out.print("Destinatário (username): ");
        String username = sc.nextLine().trim();
        if (!user.findUserByUsername(username)) {
            System.out.println("Usuário destino não existe.");
            return;
        }

        System.out.print("Mensagem: ");
        String mensagem = sc.nextLine();

        System.out.print("Digite a chave secreta: ");
        String secretKey = sc.nextLine();

        String encryptedMessage = encrypt(mensagem, secretKey);
        MessageEntity m = new MessageEntity(
                user.getUsername(),
                username,
                encryptedMessage,
                new Date(),
                "nao_lida"
        );
        m.sendMessage();
    }

    private Integer promptIndex(int size) {

        String raw = sc.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Índice inválido.");
            return null;
        }
        if (idx == -1) return null;
        if (idx < 0 || idx >= size) {
            System.out.println("Índice fora do intervalo.");
            return null;
        }
        return idx;
    }

    private String promptSecret() {
        System.out.print("Digite a chave secreta: ");
        return sc.nextLine();
    }

    private void exibirMensagemDetalhe(Document doc, String mensagem, boolean enviada) {
        System.out.println("\n--------------------------");
        if (enviada) {
            System.out.println("Para: " + doc.getString("receptor"));
        } else {
            System.out.println("De: " + doc.getString("transmissor"));
        }
        System.out.println("Data: " + doc.getDate("dataEnvio"));
        System.out.println("Status: " + doc.getString("status"));
        System.out.println("Mensagem: " + mensagem);
        System.out.println("--------------------------");
    }
}
