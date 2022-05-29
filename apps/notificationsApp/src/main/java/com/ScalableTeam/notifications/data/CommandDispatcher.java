package com.ScalableTeam.notifications.data;

import com.ScalableTeam.notifications.config.MessagingConfig;
import com.ScalableTeam.notifications.utils.Command;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class CommandDispatcher {

    private final Map<String, Command<Object, Object>> commandsMap;

    @RabbitListener(queues = MessagingConfig.QUEUE_NAME, returnExceptions = "true")
    public Object onMessageReceived(Message<?> message, @Header("command") String commandName) throws Exception {
        Command<Object, Object> command = commandsMap.get(commandName);
        return command.execute(message.getPayload());
    }
}
