package com.ScalableTeam.notifications;

import com.ScalableTeam.notifications.commands.SendNotificationCommand;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NotificationsApplication.class)
class NotificationsAppTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        // TODO
    }

    @AfterEach
    void tearDown() throws ExecutionException, InterruptedException {
        // TODO
    }

    @Test
    void givenNotification_whenSend_returnSuccess() throws Exception {
        SendNotificationCommand sendNotificationCommand = context.getBean(SendNotificationCommand.class);
        int result = sendNotificationCommand.execute(FakeData.NOTIFICATION_SEND_REQUEST);

        assertEquals(200, result);
    }
}
