package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.reddit.ICommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class GetPostCommentsService implements ICommand<String, Collection<Comment>> {
    private final PostRepository postRepository;

    @RabbitListener(queues = "${mq.queues.request.reddit.getPostComments}", returnExceptions = "true")
    public Collection<Comment> execute(String postId, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Get Post Comments Form={}", commandName, correlationId, postId);
        return execute(postId);
    }

    @Override
    public Collection<Comment> execute(String postId) throws Exception {
        log.info("Service::Get Post Comments Form={}", postId);
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new IllegalStateException(String.format("Post with id %s does not exist", postId));
        }
        return post.get().getComments();
    }
}
