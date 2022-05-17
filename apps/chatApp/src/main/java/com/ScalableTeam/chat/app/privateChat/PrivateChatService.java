package com.ScalableTeam.chat.app.privateChat;

import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PrivateChatService {

    public String createChat(Map<String, List<String>> users) {
        try {
            List<String> userIds = users.get("userIds");
            PrivateChat privateChat = new PrivateChat(userIds);
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                    .add(privateChat);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }

    }

    public String addMessage(Map<String, String> newMessage) {
        try {
            Message m = new Message(newMessage.get("authorId"), newMessage.get("content"), Timestamp.now());
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                    .document(newMessage.get("privateChatId"))
                    .collection("Messages").add(m);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }

    }

    public List<Message> getChat(String privateChatId, String lastMessageId) {
        try {
            System.out.println(privateChatId);
            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("PrivateChats")
                    .document(privateChatId)
                    .collection("Messages")
                    .document(lastMessageId);
            System.out.println("DOC " + docRef);

            Query query = database.collection("PrivateChats")
                    .document(privateChatId)
                    .collection("Messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(docRef.get().get())
                    .limit(10);

            List<QueryDocumentSnapshot> messages = query.get().get().getDocuments();

            System.out.println(messages.size());
            ArrayList<Message> messagesOut = new ArrayList<>();
            for (QueryDocumentSnapshot q : messages) {
                Map<String, Object> m = q.getData();
                System.out.println(m);
                messagesOut.add(new Message(q.getId(), (String) m.get("authorId"), (String) m.get("content"), (Timestamp) m.get("timestamp")));
            }


            return messagesOut;


        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public List<PrivateChat> getAllChats(String userId) {
        try {
            final Firestore database = FirestoreClient.getFirestore();

            Query query = database.collection("PrivateChats").whereArrayContains("users", userId);

            List<QueryDocumentSnapshot> chats = query.get().get().getDocuments();


            List<PrivateChat> chatsOut = new ArrayList<>();
            for (QueryDocumentSnapshot q : chats) {
                Map<String, Object> c = q.getData();
                System.out.println(q.getId());
                chatsOut.add(new PrivateChat(q.getId()));
            }

            return chatsOut;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
