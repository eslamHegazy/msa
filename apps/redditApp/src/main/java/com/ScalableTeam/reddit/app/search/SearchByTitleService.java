package com.ScalableTeam.reddit.app.search;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class SearchByTitleService implements MyCommand {
    @Autowired
    private PostRepository postRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit.searchByTitle}", returnExceptions = "true")
    public String listenToRequestQueue(String title, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Search By Title Form={}", commandName, correlationId, title);
        return execute(title);
    }

    @Override
    public String execute(Object title) throws Exception {
        log.info("Service::Search By Title Form={}", title);
        return Arrays.toString(postRepository.getPostsByPostTitle((String) title));
    }
}
