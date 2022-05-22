package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.requestForms.VoteCommentForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class GetPostCommentsService implements MyCommand {
    private final PostRepository postRepository;
    private final GeneralConfig generalConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.getPostComments}")
    public Collection<Comment> execute(String postId, Message message) throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("postId", postId);
        attributes.put("message", message);
        return (Collection<Comment>) execute(attributes);
    }

    @Override
    public Object execute(Object obj) throws Exception {
        Map<String, Object> attributes = (Map<String, Object>) obj;
        String postId = (String) attributes.get("postId");
        Message message = (Message) attributes.get("message");
        String correlationId = message.getMessageProperties().getCorrelationId();

        String indicator = generalConfig.getCommands().get("getPostComments");
        log.info(indicator + "Service::Post Id={}, CorrelationId={}", postId, correlationId);

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new IllegalStateException(String.format("Post with id %s does not exist", postId));
        }
        return post.get().getComments();
    }
}
