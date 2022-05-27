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
public class SearchByChannelService implements MyCommand {
    private PostRepository postRepository;
    @Autowired
    private GeneralConfig generalConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.searchByChannel}")
    public String listenToRequestQueue(String id, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("searchByChannel");

        log.info(indicator + "Service::Search By Channel, CorrelationId={}", correlationId);
        return execute(id);
    }

    @Override
    public String execute(Object id) throws Exception {
        return Arrays.toString(postRepository.getPostsByChannelId((String) id));
    }
}
