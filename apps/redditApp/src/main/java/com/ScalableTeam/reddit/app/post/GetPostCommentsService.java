package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.ICommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class GetPostCommentsService implements ICommand<String, Collection<Comment>> {
    private final PostRepository postRepository;
    private final GeneralConfig generalConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.getPostComments}", returnExceptions = "true")
    public Collection<Comment> execute(String postId, Message message) throws Exception {
        String indicator = generalConfig.getCommands().get("getPostComments");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Queue Listener::Post Id={}, CorrelationId={}", postId, correlationId);
        return execute(postId);
    }

    @Override
    public Collection<Comment> execute(String postId) throws Exception {
        String indicator = generalConfig.getCommands().get("getPostComments");
        log.info(indicator + "Service::Post Id={}, CorrelationId={}", postId);

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new IllegalStateException(String.format("Post with id %s does not exist", postId));
        }
        return post.get().getComments();
    }
}
