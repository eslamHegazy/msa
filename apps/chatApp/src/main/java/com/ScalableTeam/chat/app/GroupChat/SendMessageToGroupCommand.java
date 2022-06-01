package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SendMessageToGroupCommand implements MyCommand {

    public RabbitMQProducer rabbitMQProducer;

    @Autowired
    public SendMessageToGroupCommand(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

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

            Map<String, Object> docSnap = database.collection("GroupChats")
                    .document(groupChatId).get().get().getData();

            List<String> users = (List<String>) docSnap.get("users");

            rabbitMQProducer.publishSynchronous(MessageQueues.NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "New Chat Message",
                    content,
                    authorId,
                    users
            ));

            return addedDocRef.get().getId();
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
