package com.ScalableTeam.notifications.data;

import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import org.springframework.stereotype.Repository;

@Repository
public class FakeNotificationsRepository extends NotificationsRepository {

    @Override
    protected void notifyUser(NotificationSendRequest notification) {
        System.out.println("Sent notification: " + notification.toString());
    }
}
