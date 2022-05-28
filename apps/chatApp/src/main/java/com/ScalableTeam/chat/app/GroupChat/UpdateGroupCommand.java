package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateGroupCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> groupChatDetails) {
        try {
            System.out.println(groupChatDetails);

            String chatId = (String) groupChatDetails.getOrDefault("id", null);
            if (chatId == null) {
                return "Cannot Update Group";
            }
            String name = (String) groupChatDetails.getOrDefault("name", null);
            String description = (String) groupChatDetails.getOrDefault("description", null);

            HashMap<String, String> chatUpdates = new HashMap<>();
            if (name != null)
                chatUpdates.put("name", name);
            if (description != null)
                chatUpdates.put("description", description);

            final Firestore database = FirestoreClient.getFirestore();

            DocumentReference documentReference = database.collection("GroupChats").document(chatId);
            ApiFuture<WriteResult> updatedDocRef = documentReference.set(chatUpdates, SetOptions.merge());
            System.out.println("Updated document with ID: " + updatedDocRef.get());

            DocumentReference updatedDocumentReference = database.collection("GroupChats").document(chatId);

            return updatedDocumentReference.get().get().getData();
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
