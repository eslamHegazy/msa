package com.ScalableTeam.chat.app.GroupChat;

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
public class GetMessageByGroupIdCommand implements MyCommand {
    @Override
    public Object execute(Map<String, Object> object) {
        try {
            String groupChatId = (String) object.get("chatId");
            String lastMessageId = (String) object.get("lastMessageId");
            System.out.println("GROUP CHAT ID" + groupChatId);
            System.out.println("Last Message ID" + lastMessageId);
            if (groupChatId.length() == 0 || lastMessageId.length() == 0) {
                return new ArrayList<>();
            }
            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("GroupChats")
                    .document(groupChatId)
                    .collection("Messages")
                    .document(lastMessageId);
            System.out.println("DOC " + docRef);

            Query query = database.collection("GroupChats")
                    .document(groupChatId)
                    .collection("Messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(docRef.get().get())
                    .limit(10);

            List<QueryDocumentSnapshot> messages = query.get().get().getDocuments();
            ArrayList<Message> messagesOut = new ArrayList<>();
            for (QueryDocumentSnapshot q : messages) {
                Map<String, Object> m = q.getData();
                messagesOut.add(new Message(q.getId(), (String) m.get("authorId"), (String) m.get("content"), (Timestamp) m.get("timestamp")));
            }

            System.out.println(messagesOut);
            return messagesOut;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
