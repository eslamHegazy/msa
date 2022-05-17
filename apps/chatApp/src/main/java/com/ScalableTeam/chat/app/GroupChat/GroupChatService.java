package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.entity.GroupChat;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupChatService {

    public String createGroup(Map<String, Object> groupChatDetails) {
        try {
            System.out.println(groupChatDetails);

            String name = (String) groupChatDetails.get("name");
            String description = (String) groupChatDetails.get("description");
            String adminId = (String) groupChatDetails.get("adminId");

            ArrayList<String> userIds = (ArrayList<String>) groupChatDetails.get("userIds");
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

    public String updateGroup(Map<String, Object> groupChatDetails) {
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
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }

    public String deleteGroup(String groupChatId) {
        try {
            final Firestore database = FirestoreClient.getFirestore();

            ApiFuture<WriteResult> deletedDocRef = database.collection("GroupChats").document(groupChatId).delete();
            System.out.println("Deleted document with ID: " + deletedDocRef.get());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }

    public String sendMessageToGroup(Map<String, String> newMessage) {
        try {
            String authorId = newMessage.getOrDefault("authorId", null);
            String content = newMessage.getOrDefault("content", null);

            String groupChatId = newMessage.getOrDefault("groupChatId", null);
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

    public List<Message> getMessagesByGroupId(String groupChatId, String lastMessageId) {
        try {
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
                System.out.println(m);
                messagesOut.add(new Message(q.getId(), (String) m.get("authorId"), (String) m.get("content"), (Timestamp) m.get("timestamp")));
            }
            return messagesOut;

        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public String addMember(Map<String, String> groupChatDetails) {
        try {
            String groupChatId = groupChatDetails.get("groupId");
            String memberId = groupChatDetails.get("memberId");

            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("GroupChats").document(groupChatId);

            ApiFuture<WriteResult> arrayUnion = docRef.update("userIds", FieldValue.arrayUnion(memberId));
            System.out.println("Update time : " + arrayUnion.get());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }

    public String removeMember(Map<String, String> groupChatDetails) {
        try {
            String groupChatId = groupChatDetails.get("groupId");
            String memberId = groupChatDetails.get("memberId");
            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("GroupChats").document(groupChatId);

            ApiFuture<WriteResult> arrayUnion = docRef.update("userIds", FieldValue.arrayRemove(memberId));
            System.out.println("Update time : " + arrayUnion.get());
            return "0";
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
