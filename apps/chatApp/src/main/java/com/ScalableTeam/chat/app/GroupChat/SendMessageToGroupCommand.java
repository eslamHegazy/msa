package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.Map;

public class SendMessageToGroupCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> newMessage) {
        try {
            String authorId = (String) newMessage.getOrDefault("authorId", null);
            String content = (String) newMessage.getOrDefault("content", null);

            String groupChatId = (String) newMessage.getOrDefault("groupChatId", null);
            if (groupChatId == null || authorId == null || content == null) {
                return "Failed to send message";
            }
            Message m = new Message(authorId, content, Timestamp.now());
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<DocumentReference> addedDocRef = database.collection("GroupChats")
                    .document(groupChatId)
                    .collection("Messages").add(m);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
