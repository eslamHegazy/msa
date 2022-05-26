package com.ScalableTeam.chat.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataPopulation {
    final static Firestore database = FirestoreClient.getFirestore();

    public static void populateDB() throws ExecutionException, InterruptedException {
        String pvtChatId = createPrivateChat();
        String grpChatId = createGroupChat();
        addMessageToPrivateChat(pvtChatId);
        addMessageToGroupChat(grpChatId);
        addMembersToGroupChat(grpChatId);
    }

    private static String createPrivateChat() throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                .add(Data.pvChat);
        System.out.println("Added document with ID: " + addedDocRef.get().getId());

        return addedDocRef.get().getId();
    }

    private static String createGroupChat() throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> addedDocRef = database.collection("GroupChats")
                .add(Data.grpChat);
        System.out.println("Added document with ID: " + addedDocRef.get().getId());

        return addedDocRef.get().getId();
    }

    private static void addMessageToPrivateChat(String privateChatId) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                .document(privateChatId)
                .collection("Messages").add(Data.m1);
        System.out.println("Added document with ID: " + addedDocRef.get().getId());
    }

    private static void addMessageToGroupChat(String groupChatId) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> addedDocRef = database.collection("GroupChats")
                .document(groupChatId)
                .collection("Messages").add(Data.m2);

        System.out.println("Added document with ID: " + addedDocRef.get().getId());
    }

    private static void addMembersToGroupChat(String groupChatId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = database.collection("GroupChats").document(groupChatId);

        ApiFuture<WriteResult> arrayUnion = docRef.update("users", FieldValue.arrayUnion("1", "2"));
        System.out.println("Update time : " + arrayUnion.get());
    }

    public static void clearDB() throws ExecutionException, InterruptedException {
        // clear private chats
        ApiFuture<QuerySnapshot> future = database.collection("PrivateChats").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete();
        }

        //  clear group chats
        future = database.collection("GroupChats").get();
        documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete();
        }
    }
}
