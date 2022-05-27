package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetNotificationsCommand implements Command {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public List<NotificationResponse> execute(Object body) throws Exception {
        String userId = String.valueOf(body);
        return notificationsRepository.getNotifications(userId);
    }

    @RabbitListener(queues = "${mq.queues.request.notifications.getNotifications}")
    public List<NotificationResponse> onMessageReceived(String userId, Message message) throws Exception {
        String indicator = generalConfig.getCommands().get("getNotifications");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Request::CorrelationId: {}", correlationId);
        return execute(userId);
    }

    @RabbitListener(queues = "${mq.queues.response.notifications.getNotifications}")
    public void onMessageSent(List<NotificationResponse> response, Message message) {
        String indicator = generalConfig.getCommands().get("getNotifications");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::Response::CorrelationId: {}, Message: {}", correlationId, response);
    }
}
