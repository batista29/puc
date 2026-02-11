package org.puc;

import com.mongodb.client.MongoCollection;
import org.puc.user.entity.UserEntity;
import org.puc.view.MenuInitial;

import static org.puc.database.EncryptionHandler.decrypt;
import static org.puc.database.EncryptionHandler.encrypt;
import org.puc.database.MongoHandler;

import org.bson.Document;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Encerrando conexão com o MongoDB...");
            MongoHandler.close();
        }));

        UserEntity user = new UserEntity("", "", "", "");
        MenuInitial menuInitial = new MenuInitial();
        menuInitial.exibir(user);
    }
}