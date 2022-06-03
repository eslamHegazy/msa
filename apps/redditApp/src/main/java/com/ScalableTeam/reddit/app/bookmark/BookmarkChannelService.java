package com.ScalableTeam.reddit.app.bookmark;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.BookmarkChannelForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@AllArgsConstructor
public class BookmarkChannelService implements MyCommand {
    private final String serviceName = "bookmarkChannel";
    private UserRepository userRepository;
    private ChannelRepository channelRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}")
    public String listenToRequestQueue(BookmarkChannelForm bookmarkChannelForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Bookmark Post Form={}", commandName, correlationId, bookmarkChannelForm);
        return execute(bookmarkChannelForm);
    }

    @Override
    public String execute(Object body) {
        BookmarkChannelForm request = (BookmarkChannelForm) body;

        log.info("Service::Bookmark channel form ={}", request);


        if (channelRepository.findById(request.getChannelId()).isEmpty()) {
            return "channel not found";
        }
        HashMap<String, Boolean> bookmark = new HashMap<String, Boolean>();
        bookmark.put(request.getChannelId(), true);
        userRepository.updateBookmarkedChannels(request.getUserId(), bookmark);

        return "Channel Bookmarked successfully";
    }

    @RabbitListener(queues = "${mq.queues.response.reddit." + serviceName + "}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
