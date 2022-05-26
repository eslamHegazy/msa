package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.GroupChat;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.Map;

public class CreateGroupCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> groupChatDetails) {
        try {
            System.out.println(groupChatDetails);

            String name = (String) groupChatDetails.get("name");
            String description = (String) groupChatDetails.get("description");
            String adminId = (String) groupChatDetails.get("adminId");

            ArrayList<String> userIds = (ArrayList<String>) groupChatDetails.get("users");
            GroupChat groupChat = new GroupChat(name, description, adminId, userIds);
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<DocumentReference> addedDocRef = database.collection("GroupChats")
                    .add(groupChat);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }

    }
}
