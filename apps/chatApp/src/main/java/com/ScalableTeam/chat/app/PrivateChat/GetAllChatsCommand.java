package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.GroupChat;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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
                chatsOut.add(new PrivateChat(q.getId(), (ArrayList<String>) c.get("users")));
            }

            Query query2 = database.collection("GroupChats").whereArrayContains("users", userId);

            List<QueryDocumentSnapshot> chatsGrp = query2.get().get().getDocuments();

            List<GroupChat> grpChatsOut = new ArrayList<>();

            for (QueryDocumentSnapshot q : chatsGrp) {
                Map<String, Object> c = q.getData();
                String chatName = (String) c.get("name");
                String chatDesc = (String) c.get("description");
                String adminId = (String) c.get("adminId");
                List<String> users = (List<String>) c.get("users");

                grpChatsOut.add(new GroupChat(q.getId(), chatName, chatDesc, adminId, users));
            }

            Map<String, Object> out = new HashMap<>();
            out.put("PrivateChats", chatsOut);
            out.put("GroupChats", grpChatsOut);
            return out;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
