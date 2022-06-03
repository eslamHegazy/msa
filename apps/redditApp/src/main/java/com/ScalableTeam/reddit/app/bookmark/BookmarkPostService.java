package com.ScalableTeam.reddit.app.bookmark;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.BookmarkPostForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
@AllArgsConstructor
public class BookmarkPostService implements MyCommand {

    private final String serviceName = "bookmarkPost";
    private UserRepository userRepository;
    private PostRepository postRepository;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}")
    public String listenToRequestQueue(BookmarkPostForm bookmarkPostForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Bookmark Post Form={}", commandName, correlationId, bookmarkPostForm);
        return execute(bookmarkPostForm);
    }


    @Override
    public String execute(Object body) {
        BookmarkPostForm request = (BookmarkPostForm) body;
        log.info("Service::Bookmark post form ={}", request);


        if (postRepository.findById(request.getPostId()).isEmpty()) {
            return "post not found";
        }
        HashMap<String, Boolean> bookmark = new HashMap<String, Boolean>();
        bookmark.put(request.getPostId(), true);
        userRepository.updateBookmarkedPosts(request.getUserId(), bookmark);


        return "Post Bookmarked successfully";
    }

    @RabbitListener(queues = "${mq.queues.response.reddit." + serviceName + "}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
