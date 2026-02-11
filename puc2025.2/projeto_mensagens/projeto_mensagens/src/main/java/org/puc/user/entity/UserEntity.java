package org.puc.user.entity;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;

import static org.puc.database.EncryptionHandler.encryptPass;
import static org.puc.database.MongoHandler.getCollection;

public class UserEntity {
    private String nome;
    private String email;
    private String username;
    private String senha;

    public UserEntity(String nome, String email, String username, String senha){
        this.nome = nome == null ? "" : nome;
        this.username = username == null ? "" : username;
        this.email = email;
        this.senha = senha;
    }

    public String getNome(){
        return nome;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail(){
        return email;
    }
    public String getSenha(){
        return senha;
    }

    public void setNome(String nome) throws Exception{
        if(nome == null) throw new Exception("nome nulo!");
        this.nome = nome;
    }

    public void setEmail(String email) throws Exception{
        if(email == null) throw new Exception("email nulo!");
        this.email = email;
    }
    public void setUsername(String username) throws Exception{
        if(username == null) throw new Exception("username nulo!");
        this.username = username;
    }
    public void setSenha(String senha) throws Exception{
        if(senha == null) throw new Exception("senha nulo!");
        this.senha = senha;
    }

    public void cadastrarUsuario(){
        try {
            MongoCollection<Document> collection = getCollection("Users");

            String senhaCriptografada = encryptPass(this.senha);

            Document novoUser = new Document("nome", this.nome)
                    .append("username", this.username)
                    .append("email", this.email)
                    .append("senha", senhaCriptografada);
            System.out.println("Inserindo o documento...");

            InsertOneResult result = collection.insertOne(novoUser);
            if(result.getInsertedId() != null){
                System.out.println("Documento Inserido com sucesso!");
            }
        }catch (Exception e){
            System.out.println("Erro ao inserir novo documento!");
            e.printStackTrace();
        }
    }
    public Document logar() {
        try {
            MongoCollection<Document> collection = getCollection("Users");

            Document user = collection.find(Filters.eq("email", this.email)).first();

            if (user == null) {
                System.out.println("Usuário não encontrado!");
                return null;
            }

            String senhaBanco = user.getString("senha");
            String senhaDigitadaCript = encryptPass(this.senha);

            if (!senhaDigitadaCript.equals(senhaBanco)) {
                System.out.println("Senha incorreta!");
                return null;
            }

            System.out.println("Usuário logado!");
            return user;

        }catch(Exception e){
            System.out.println("Erro ao logar usuário!");
            e.printStackTrace();
            return null;
        }

    }
    public boolean findUserByUsername(String username) {
        try {
            MongoCollection<Document> collection = getCollection("Users");
            Document found = collection.find(Filters.eq("username",username)).first();
            if(found != null) {
                return true;
            } else {
                System.out.println("Usuário nao encontrado!");
                return false;
            }
        }catch(Exception e){
            System.out.println("Erro ao procurar usuário!");
            e.printStackTrace();
            return false;
        }

    }
}
