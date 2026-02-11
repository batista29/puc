package org.puc.database;
import com.mongodb.client.*;

import org.bson.Document;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class MongoHandler {
    private static final String CONNECTION_STRING = System.getenv("MONGO_URI");
    private static final String DATABASE_NAME = System.getenv("DATABASE_NAME");

    private static volatile MongoClient mongoClient;
    private static volatile MongoDatabase database;

    private static void ensureConnection() {
        if (mongoClient == null || database == null) {
            synchronized (MongoHandler.class) {
                if (mongoClient == null) {
                    mongoClient = MongoClients.create(CONNECTION_STRING);
                    database = mongoClient.getDatabase(DATABASE_NAME);
                }
            }
        }
    }

    public static MongoCollection<Document> getCollection(String name) {
        ensureConnection();
        return database.getCollection(name);
    }

    public static List<Document> findDocuments(String collectionName, Document filter) {
        ensureConnection();
        List<Document> results = new ArrayList<>();
        getCollection(collectionName).find(filter).into(results);
        return results;
    }

    public static long updateOne(String collectionName, Document filter, Document fieldsToSet) {
        ensureConnection();
        Document update = new Document("$set", fieldsToSet);
        UpdateResult result = getCollection(collectionName).updateOne(filter, update);
        return result.getModifiedCount();
    }

    public static void close() {
        if (mongoClient != null) {
            synchronized (MongoHandler.class) {
                if (mongoClient != null) {
                    mongoClient.close();
                    mongoClient = null;
                    database = null;
                }
            }
        }
    }
}
