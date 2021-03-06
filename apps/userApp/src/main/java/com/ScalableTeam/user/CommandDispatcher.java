package com.ScalableTeam.user;

import com.ScalableTeam.user.amqp.MessageConfig;
import com.ScalableTeam.user.commands.ICommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class CommandDispatcher {
    private final Map<String, ICommand> commandMap;
    private final ConfigurableApplicationContext ctx;

    @RabbitListener(queues = MessageConfig.QUEUE, returnExceptions = "true")
    public Object receiveMessage(Message message, @Header("command") String command) {
        log.info("Executing {}", command);
        try {
            ICommand iCommand = commandMap.get(command);
            if (iCommand == null) {
                iCommand = ctx.getBeansOfType(ICommand.class).get(command);
            }
            if (iCommand == null) {
                System.out.println("Command does not exist");
                return null;
            }
            return iCommand.execute(message.getPayload());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return null;
        }
    }
}
