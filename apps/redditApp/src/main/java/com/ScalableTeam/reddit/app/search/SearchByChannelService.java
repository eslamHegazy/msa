package com.ScalableTeam.reddit.app.search;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class SearchByChannelService implements MyCommand {
    private PostRepository postRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit.searchByChannel}", returnExceptions = "true")
    public String listenToRequestQueue(String id, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Search By Channel Form={}", commandName, correlationId, id);
        return execute(id);
    }

    @Override
    public String execute(Object id) throws Exception {
        log.info("Service::Search By Channel Form={}", id);
        return Arrays.toString(postRepository.getPostsByChannelId((String) id));
    }
}
