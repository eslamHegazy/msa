package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteNotificationCommand implements Command<NotificationDeleteRequest, Integer> {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(NotificationDeleteRequest body) throws Exception {
        notificationsRepository.deleteNotification(body);
        return 200;
    }

    @RabbitListener(queues = "${mq.queues.request.notifications.deleteNotification}")
    public Integer onMessageReceived(NotificationDeleteRequest notificationDeleteRequest, Message message) throws Exception {
        String indicator = generalConfig.getCommands().get("deleteNotification");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Request::CorrelationId: {}", correlationId);
        return execute(notificationDeleteRequest);
    }

    @RabbitListener(queues = "${mq.queues.response.notifications.deleteNotification}")
    public void onMessageSent(Integer response, Message message) {
        String indicator = generalConfig.getCommands().get("deleteNotification");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Response::CorrelationId: {}, Message: {}", correlationId, response);
    }
}
