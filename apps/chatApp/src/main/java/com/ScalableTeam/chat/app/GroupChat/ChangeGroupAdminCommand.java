package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChangeGroupAdminCommand implements MyCommand {

    public RabbitMQProducer rabbitMQProducer;

    @Autowired
    public ChangeGroupAdminCommand(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }


    @Override
    public Object execute(Map<String, Object> groupChatDetails) {
        try {
            System.out.println("ADMIN " + groupChatDetails);

            String chatId = (String) groupChatDetails.getOrDefault("id", null);
            if (chatId == null) {
                return "Cannot Change Group Admin";
            }
            String newAdminId = (String) groupChatDetails.getOrDefault("newAdminId", null);

            HashMap<String, String> chatUpdates = new HashMap<>();
            if (newAdminId != null)
                chatUpdates.put("adminId", newAdminId);
            else
                return "No New Admin Specified";

            final Firestore database = FirestoreClient.getFirestore();

            DocumentReference documentReference = database.collection("GroupChats").document(chatId);
            ApiFuture<WriteResult> updatedDocRef = documentReference.set(chatUpdates, SetOptions.merge());
            System.out.println("Updated document with ID: " + updatedDocRef.get());

            DocumentReference updatedDocumentReference = database.collection("GroupChats").document(chatId);

            Map<String, Object> docSnap = database.collection("GroupChats")
                    .document(chatId).get().get().getData();

            List<String> users = (List<String>) docSnap.get("users");

            rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "New Group Notification for group " + docSnap.get("name"),
                    "Group Admin Changed to " + newAdminId,
                    "Group Notification " + docSnap.get("name"),
                    users
            ), MessageQueues.RESPONSE_NOTIFICATIONS);

            return updatedDocumentReference.get().get().getData();
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
