package com.ScalableTeam.user.amqp;

import com.ScalableTeam.user.amqp.MessageConfig;
import com.ScalableTeam.user.amqp.commands.ICommand;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class CommandDispatcher {
    private final Map<String, ICommand> commandMap;

    @RabbitListener(queues = MessageConfig.QUEUE)
    public Object receiveMessage(Message message, @Header("command") String command) {
        ICommand iCommand = commandMap.get(command);
        return iCommand.execute(message.getPayload());
    }
}
