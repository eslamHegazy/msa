package com.ScalableTeam.notifications.messaging;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.notifications.utils.Command;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class CommandDispatcher {

    private static final String HEADER_COMMAND = "Command";
    private final Map<String, Command> commandsMap;
    private final ConfigurableApplicationContext ctx;

    @RabbitListener(queues = MessageQueues.REQUEST_NOTIFICATIONS, returnExceptions = "true")
    public Object onRequestReceived(Message message, @Header(HEADER_COMMAND) String commandName) throws Exception {
        Command command = commandsMap.get(commandName);
        if (command == null) {
            command = ctx.getBeansOfType(Command.class).get(commandName);
        }
        if (command == null) {
            System.out.println("Command does not exist");
            return null;
        }
        log.info("Service: Notifications, Queue: {}, Message: {}, Payload: {}",
                MessageQueues.REQUEST_NOTIFICATIONS,
                command.getClass().getCanonicalName(),
                message.getPayload());
        return command.execute(message.getPayload());
    }

    @RabbitListener(queues = MessageQueues.RESPONSE_NOTIFICATIONS, returnExceptions = "true")
    public void onResponseReceived(Message message) {
        log.info("Service: Notifications, Queue: {}, Payload: {}",
                MessageQueues.RESPONSE_NOTIFICATIONS,
                message.getPayload());
    }
}
