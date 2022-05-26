package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetAllChatsCommand implements MyCommand {

    @Override
    public Object execute(Map<String, Object> object) {
        try {
            String userId = (String) object.get("userId");
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
