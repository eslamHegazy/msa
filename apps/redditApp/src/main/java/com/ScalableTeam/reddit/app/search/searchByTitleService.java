package com.ScalableTeam.reddit.app.search;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class searchByTitleService implements MyCommand {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GeneralConfig generalConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.searchByTitle}")
    public String listenToRequestQueue(String title, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("searchByTitle");

        log.info(indicator + "Service::Search By Title, CorrelationId={}", correlationId);
        return execute(title);
    }

    @Override
    public String execute(Object title) throws Exception {
        return Arrays.toString(postRepository.getPostsByPostTitle((String) title));
    }
}
