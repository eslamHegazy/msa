package com.ScalableTeam.chat.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBMethods {

    final static Firestore database = FirestoreClient.getFirestore();

    public static int countDocuments(String collectionName) throws ExecutionException, InterruptedException {
        // count of documents in collection should be 2
        ApiFuture<QuerySnapshot> future = database.collection(collectionName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.size();
    }

    public static Object checkDocExists(String collectionName, String id) throws ExecutionException, InterruptedException {
        Object doc = database.collection(collectionName)
                .document(id).get().get().getData();
        System.out.println(doc);
        return doc;
    }

    public static Object checkMsgDocExists(String collectionName, String chatId,String msgId) throws ExecutionException, InterruptedException {
        Object doc = database.collection(collectionName)
                .document(chatId).collection("Messages").document(msgId).get().get().getData();
        System.out.println(doc);
        return doc;
    }

}
