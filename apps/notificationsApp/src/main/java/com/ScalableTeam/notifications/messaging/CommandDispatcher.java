package com.ScalableTeam.notifications.messaging;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.notifications.utils.Command;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class CommandDispatcher {

    private final Map<String, Command<Object, Object>> commandsMap;

    @RabbitListener(queues = MessageQueues.NOTIFICATIONS, returnExceptions = "true")
    public Object onMessageReceived(Message<?> message, @Header("command") String commandName) throws Exception {
        Command<Object, Object> command = commandsMap.get(commandName);
        log.info("Service: Notifications, Message: {}, Payload: {}",
                command.getClass().getCanonicalName(),
                message.getPayload());
        return command.execute(message.getPayload());
    }
}
