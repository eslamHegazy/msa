package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CreateChatCommand implements MyCommand {

    public CreateChatCommand() {
    }

    @Override
    public Object execute(Map<String, Object> reqBody) {
        try {
            System.out.println("object received: " + reqBody);
            List<String> userIds = (List<String>) reqBody.get("users");
            PrivateChat privateChat = new PrivateChat(userIds);
            final Firestore database = FirestoreClient.getFirestore();
            ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                    .add(privateChat);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());
            return addedDocRef.get().getId();
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
