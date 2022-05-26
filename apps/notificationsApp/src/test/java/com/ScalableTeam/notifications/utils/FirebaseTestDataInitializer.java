package com.ScalableTeam.notifications.utils;

import com.ScalableTeam.notifications.constants.Collections;
import com.ScalableTeam.notifications.constants.Fields;
import com.ScalableTeam.notifications.data.FakeData;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseTestDataInitializer {

    private static final Firestore firestore = FirestoreClient.getFirestore();

    public static void populateFakeData() throws ExecutionException, InterruptedException {
        clearFakeData();

        List<ApiFuture<WriteResult>> futures = new ArrayList<>();

        // Add users tokens.
        futures.add(firestore.collection(Collections.USERS).document(FakeData.USER_ID_1).set(Map.of(Fields.TOKENS, List.of(FakeData.DEVICE_TOKEN_1))));
        futures.add(firestore.collection(Collections.USERS).document(FakeData.USER_ID_2).set(Map.of(Fields.TOKENS, List.of(FakeData.DEVICE_TOKEN_2))));

        // Store a notification.
        HashMap<String, Object> document = new HashMap<>();

        NotificationSendRequest notification = FakeData.NOTIFICATION_SEND_REQUEST;

        document.put(Fields.SENDER, notification.getSender());
        document.put(Fields.TITLE, notification.getTitle());
        document.put(Fields.BODY, notification.getBody());
        document.put(Fields.TIMESTAMP, FieldValue.serverTimestamp());
        document.put(Fields.IS_READ, false);

        for (String receiver : notification.getReceivers()) {
            futures.add(firestore.collection(Collections.USERS).document(receiver).collection(Collections.NOTIFICATIONS).document(FakeData.NOTIFICATION_ID).set(document));
        }

        ApiFutures.allAsList(futures).get();
    }

    public static void clearFakeData() throws ExecutionException, InterruptedException {
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();

        futures.add(firestore.collection(Collections.USERS).document(FakeData.USER_ID_1).delete());
        futures.add(firestore.collection(Collections.USERS).document(FakeData.USER_ID_2).delete());

        ApiFutures.allAsList(futures).get();
    }
}
