package com.ScalableTeam.notifications;

import com.ScalableTeam.models.notifications.requests.DeviceTokenRequest;
import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.models.notifications.responses.NotificationResponse;
import com.ScalableTeam.notifications.commands.*;
import com.ScalableTeam.notifications.constants.Collections;
import com.ScalableTeam.notifications.constants.Fields;
import com.ScalableTeam.notifications.data.FakeData;
import com.ScalableTeam.notifications.di.TestBeansConfig;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;

@SuppressWarnings("all")
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
    void givenNotification_whenSend_returnSuccess() {
        // Given
        NotificationSendRequest notificationSendRequest = FakeData.NOTIFICATION_SEND_REQUEST;

        // When

        // Send a notification to user 2.
        SendNotificationCommand sendNotificationCommand = context.getBean(SendNotificationCommand.class);
        ResponseEntity<HttpStatus> sendNotificationResult = sendNotificationCommand.execute(notificationSendRequest);

        // Get user 2 notifications.
        GetNotificationsCommand getNotificationsCommand = context.getBean(GetNotificationsCommand.class);
        ResponseEntity<List<NotificationResponse>> user2Notifications = (ResponseEntity<List<NotificationResponse>>) getNotificationsCommand.execute(FakeData.USER_ID_2);

        // Then

        // Notification is sent.
        assertThat(sendNotificationResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        // User 2 notifications list is valid. It already has one notification at start, but then a 2nd notification is
        // also created.
        assertThat(user2Notifications.getBody()).hasSize(2);

        // Validate the newly sent notification.
        NotificationResponse notificationResponse = Objects.requireNonNull(user2Notifications.getBody()).stream()
                .filter(it -> !it.getId().equals(FakeData.NOTIFICATION_RESPONSE.getId()))
                .findFirst().orElseThrow();

        assertThat(notificationResponse.getTitle()).isEqualTo(notificationSendRequest.getTitle());
        assertThat(notificationResponse.getBody()).isEqualTo(notificationSendRequest.getBody());
        assertThat(notificationResponse.getSender()).isEqualTo(notificationSendRequest.getSender());
        assertThat(notificationResponse.isRead()).isFalse();
    }

    @Test
    void givenNotification_whenGet_returnList() {
        // Given
        String user2Id = FakeData.USER_ID_2;

        // When

        // Get user 2 notifications.
        GetNotificationsCommand getNotificationsCommand = context.getBean(GetNotificationsCommand.class);
        ResponseEntity<List<NotificationResponse>> user2Notifications = (ResponseEntity<List<NotificationResponse>>) getNotificationsCommand.execute(user2Id);

        // Then
        assertThat(user2Notifications.getBody()).hasSize(1);
    }

    @Test
    void givenNotification_whenMarkAsRead_returnSuccess() {
        // Given
        NotificationReadRequest notificationReadRequest = FakeData.NOTIFICATION_READ_REQUEST;

        // When

        // Mark user 2 fake notification as read.
        MarkNotificationAsReadCommand markNotificationAsReadCommand = context.getBean(MarkNotificationAsReadCommand.class);
        ResponseEntity<HttpStatus> markNotificationAsReadResult = markNotificationAsReadCommand.execute(notificationReadRequest);

        // Get user 2 notifications.
        GetNotificationsCommand getNotificationsCommand = context.getBean(GetNotificationsCommand.class);
        ResponseEntity<List<NotificationResponse>> user2Notifications = (ResponseEntity<List<NotificationResponse>>) getNotificationsCommand.execute(FakeData.USER_ID_2);

        // Then
        assertThat(markNotificationAsReadResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        NotificationResponse notificationResponse = Objects.requireNonNull(user2Notifications.getBody()).stream()
                .filter(it -> it.getId().equals(FakeData.NOTIFICATION_RESPONSE.getId()))
                .findFirst().orElseThrow();

        assertThat(notificationResponse.isRead()).isTrue();
    }

    @Test
    void givenNotification_whenDelete_returnSuccess() {
        // Given
        NotificationDeleteRequest notificationDeleteRequest = FakeData.NOTIFICATION_DELETE_REQUEST;

        // When

        // Delete user 2 fake notification.
        DeleteNotificationCommand deleteNotificationCommand = context.getBean(DeleteNotificationCommand.class);
        ResponseEntity<HttpStatus> deleteNotificationResult = deleteNotificationCommand.execute(notificationDeleteRequest);

        // Get user 2 notifications.
        GetNotificationsCommand getNotificationsCommand = context.getBean(GetNotificationsCommand.class);
        ResponseEntity<List<NotificationResponse>> user2Notifications = (ResponseEntity<List<NotificationResponse>>) getNotificationsCommand.execute(FakeData.USER_ID_2);

        // Then
        assertThat(deleteNotificationResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user2Notifications.getBody()).hasSize(0);
    }

    @SuppressWarnings("all")
    @Test
    void givenDeviceToken_whenRegister_addToken() throws Exception {
        // Given
        DeviceTokenRequest deviceTokenRequest = FakeData.REGISTER_DEVICE_TOKEN_REQUEST;

        // When

        // Register device token.
        RegisterDeviceTokenCommand registerDeviceTokenCommand = context.getBean(RegisterDeviceTokenCommand.class);
        ResponseEntity<HttpStatus> registerDeviceTokenResult = registerDeviceTokenCommand.execute(deviceTokenRequest);

        // Then
        assertThat(registerDeviceTokenResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<String> tokens = (List<String>) FirestoreClient.getFirestore()
                .collection(Collections.USERS)
                .document(FakeData.USER_ID_2)
                .get()
                .get()
                .get(Fields.TOKENS);

        assertThat(tokens).hasSize(2);
        assertThat(tokens).contains(deviceTokenRequest.getDeviceToken());
    }

    @SuppressWarnings("all")
    @Test
    void givenDeviceToken_whenUnregister_removeToken() throws Exception {
        // Given
        DeviceTokenRequest deviceTokenRequest = FakeData.UNREGISTER_DEVICE_TOKEN_REQUEST;

        // When

        // Unregister device token.
        UnregisterDeviceTokenCommand unregisterDeviceTokenCommand = context.getBean(UnregisterDeviceTokenCommand.class);
        ResponseEntity<HttpStatus> unregisterDeviceTokenResult = unregisterDeviceTokenCommand.execute(deviceTokenRequest);

        // Then
        assertThat(unregisterDeviceTokenResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<String> tokens = (List<String>) FirestoreClient.getFirestore()
                .collection(Collections.USERS)
                .document(FakeData.USER_ID_2)
                .get()
                .get()
                .get(Fields.TOKENS);

        assertThat(tokens).hasSize(0);
        assertThat(tokens).doesNotContain(deviceTokenRequest.getDeviceToken());
    }
}
