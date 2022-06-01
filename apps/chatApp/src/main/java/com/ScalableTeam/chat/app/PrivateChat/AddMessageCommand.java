package com.ScalableTeam.chat.app.PrivateChat;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AddMessageCommand implements MyCommand {

    public RabbitMQProducer rabbitMQProducer;

    @Autowired
    public AddMessageCommand(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @Override
    public Object execute(Map<String, Object> newMessage) {
        try {

            String authorId = (String) newMessage.get("authorId");
            String content = (String) newMessage.get("content");
            String privateChatId = (String) newMessage.get("privateChatId");
            Message m = new Message(authorId, content, Timestamp.now());
            final Firestore database = FirestoreClient.getFirestore();
            ApiFuture<DocumentReference> addedDocRef = database.collection("PrivateChats")
                    .document()
                    .collection("Messages").add(m);
            System.out.println("Added document with ID: " + addedDocRef.get().getId());

            Map<String, Object> docSnap = database.collection("PrivateChats")
                    .document(privateChatId).get().get().getData();

            List<String> users = (List<String>) docSnap.get("users");

            String receiverId = "";
            if (users.get(0).equals(authorId))
                receiverId = users.get(1);
            else
                receiverId = users.get(0);
            rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "New Chat Message",
                    content,
                    authorId,
                    Arrays.asList(receiverId)
            ), MessageQueues.RESPONSE_NOTIFICATIONS);
            return addedDocRef.get().getId();
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
