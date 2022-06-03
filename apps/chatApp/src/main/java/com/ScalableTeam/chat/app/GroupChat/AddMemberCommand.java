package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddMemberCommand implements MyCommand {


    public RabbitMQProducer rabbitMQProducer;

    @Autowired
    public AddMemberCommand(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @Override
    public Object execute(Map<String, Object> groupChatDetails) {
        try {
            String groupChatId = (String) groupChatDetails.get("groupId");
            String memberId = (String) groupChatDetails.get("memberId");

            final Firestore database = FirestoreClient.getFirestore();
            DocumentReference docRef = database.collection("GroupChats").document(groupChatId);

            ApiFuture<WriteResult> arrayUnion = docRef.update("users", FieldValue.arrayUnion(memberId));
            System.out.println("Update time : " + arrayUnion.get());

            DocumentReference updatedDocumentReference = database.collection("GroupChats").document(groupChatId);

            Map<String, Object> docSnap = database.collection("GroupChats")
                    .document(groupChatId).get().get().getData();

            List<String> users = (List<String>) docSnap.get("users");

            rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "New Member added in group " + docSnap.get("name"),
                    "Member " + memberId + " is added to group " + docSnap.get("name"),
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
