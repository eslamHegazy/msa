package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetChatCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> data) {
        try {
            String privateChatId = (String) data.get("chatId");
            String lastMessageId = (String) data.get("lastMessageId");
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
}
