package com.ScalableTeam.notifications;

import com.ScalableTeam.notifications.commands.GetNotificationsCommand;
import com.ScalableTeam.notifications.commands.SendNotificationCommand;
import com.ScalableTeam.notifications.data.FakeData;
import com.ScalableTeam.notifications.di.TestBeansConfig;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;
import com.ScalableTeam.notifications.utils.FirebaseTestDataInitializer;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;

@ExtendWith(SpringExtension.class)
@Import(TestBeansConfig.class)
@SpringBootTest(classes = NotificationsApplication.class)
class NotificationsAppTest {

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        FirebaseTestDataInitializer.populateFakeData();
    }

    @AfterEach
    void tearDown() throws ExecutionException, InterruptedException {
        FirebaseTestDataInitializer.clearFakeData();
    }

    @Test
    void givenNotification_whenSend_returnSuccess() throws Exception {
        // Given
        NotificationSendRequest fakeNotification = FakeData.NOTIFICATION_SEND_REQUEST;

        // When

        // Send a notification to user 2.
        SendNotificationCommand sendNotificationCommand = context.getBean(SendNotificationCommand.class);
        int sendNotificationResult = sendNotificationCommand.execute(fakeNotification);

        // Get user 2 notifications.
        GetNotificationsCommand getNotificationsCommand = context.getBean(GetNotificationsCommand.class);
        List<NotificationResponse> user2Notifications = getNotificationsCommand.execute(FakeData.USER_ID_2);

        // Then

        // Notification is sent.
        assertThat(sendNotificationResult).isEqualTo(200);

        // User 2 notifications list is valid.
        assertThat(user2Notifications.size()).isEqualTo(1);

        NotificationResponse notificationResponse = user2Notifications.get(0);
        assertThat(notificationResponse.getTitle()).isEqualTo(fakeNotification.getTitle());
        assertThat(notificationResponse.getBody()).isEqualTo(fakeNotification.getBody());
        assertThat(notificationResponse.getSender()).isEqualTo(fakeNotification.getSender());
        assertThat(notificationResponse.isRead()).isFalse();
    }

    @Test
    void read() throws Exception {
        int size = FirestoreClient.getFirestore().collection("test").get().get().getDocuments().size();
        assertThat(size).isEqualTo(1);

        String result = FirestoreClient.getFirestore().collection("test").document("rwPOtxstsLPgMLyB02QR").get().get().getString("name");
        assertThat(result).isEqualTo("Hello");
    }
}
