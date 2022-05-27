package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DeleteGroupCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> object) {
        try {
            String groupChatId = (String) object.get("groupChatId");
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<WriteResult> deletedDocRef = database.collection("GroupChats").document(groupChatId).delete();
            System.out.println("Deleted document with ID: " + deletedDocRef.get());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
