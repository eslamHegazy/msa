package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddMessageCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> newMessage) {
        try {
            Message m = new Message((String) newMessage.get("authorId"), (String) newMessage.get("content"), Timestamp.now());
            final Firestore database = FirestoreClient.getFirestore();
            ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                    .document((String) newMessage.get("privateChatId"))
                    .collection("Messages").add(m);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return "Success";
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
