package org.puc.message.entity;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.puc.user.entity.UserEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

import static org.puc.database.MongoHandler.getCollection;
import static org.puc.database.MongoHandler.updateOne;

public class MessageEntity {
    private String transmissor;
    private String receptor;
    private String texto;
    private Date dataEnvio;
    private String status;


    public MessageEntity(String transmissor, String receptor, String texto, Date dataEnvio, String status) {
        this.transmissor = transmissor;
        this.receptor = receptor;
        this.texto = texto;
        this.dataEnvio = dataEnvio;
        this.status = status;
    }

    public String getTransmissor() {
        return this.transmissor;
    }

    public String getReceptor() {
        return this.receptor;
    }

    public String getTexto() {
        return this.texto;
    }

    public Date getDataEnvio() {
        return this.dataEnvio;
    }

    public String getStatus() {
        return this.status;
    }

    public void setTransmissor(String transmissor) {
        this.transmissor = transmissor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void sendMessage(){
        try {
            MongoCollection<Document> collection = getCollection("Mensagens");
            Document document = new Document("transmissor", this.transmissor)
                    .append("receptor", this.receptor)
                    .append("texto", this.texto)
                    .append("dataEnvio", this.dataEnvio)
                    .append("status", this.status);
            InsertOneResult result = collection.insertOne(document);
            if (result.getInsertedId() != null) {
                System.out.println("Mensagem enviada com sucesso!");
            }else System.out.println("Erro ao enviar mensagem!");
        }catch (Exception e){
            System.out.println("Erro ao salvar mensagem!");
            e.printStackTrace();
        }
    }
    public static void marcarComoLida(String id) {
        MongoCollection<Document> collection = getCollection("Mensagens");
        Document filter = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", new Document("status", "lida"));
        collection.updateOne(filter, update);
    }
}
