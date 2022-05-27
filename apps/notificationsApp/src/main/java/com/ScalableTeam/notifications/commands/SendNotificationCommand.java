package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendNotificationCommand implements Command {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        NotificationSendRequest notification = (NotificationSendRequest) body;
        notificationsRepository.sendNotification(notification);
        return 200;
    }

    @RabbitListener(queues = "${mq.queues.request.notifications.sendNotification}")
    public Integer onMessageReceived(NotificationSendRequest notificationSendRequest, Message message) throws Exception {
        String indicator = generalConfig.getCommands().get("sendNotification");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Request::CorrelationId: {}", correlationId);
        return execute(notificationSendRequest);
    }

    @RabbitListener(queues = "${mq.queues.response.notifications.sendNotification}")
    public void onMessageSent(Integer response, Message message) {
        String indicator = generalConfig.getCommands().get("sendNotification");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Response::CorrelationId: {}, Message: {}", correlationId, response);
    }
}
