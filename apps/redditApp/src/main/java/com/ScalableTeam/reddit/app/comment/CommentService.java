package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.*;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.repository.CommentChildrenHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.CommentRepository;
import com.ScalableTeam.reddit.app.repository.PostCommentHierarchyRepository;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class CommentService implements MyCommand {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CachingService cachingService;
    @Autowired
    private CommentChildrenHierarchyRepository commentChildrenHierarchyRepository;
    @Autowired
    private PostCommentHierarchyRepository postCommentHierarchyRepository;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${mq.queues.request.reddit.comment}")
    public Post listenToRequestQueue(Comment comment, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Comment Form={}", commandName, correlationId, comment);
        return execute(comment);
    }

    @Override
    public Post execute(Object body) throws Exception {
        try {
//            CommentResponseForm commentResponseForm=(CommentResponseForm) body;
//            Comment comment=commentResponseForm.getComment();
            Comment comment = (Comment) body;
            log.info("Service::Comment Form={}", comment);

            final Optional<User> postCreatorOptional = userRepository.findById(comment.getUserNameId());
            String postId = comment.getPostId() == null ? comment.getCommentParentId() : comment.getPostId();
            final Optional<Post> postParentOptional = postRepository.findById(postId);
//            final String channelId=postRepository.getChannelOfPost(comment.getCommentParentId());
            //user has to follow the channel
            if (postCreatorOptional.isEmpty() || postParentOptional.isEmpty() ||
                    !postCreatorOptional.get().getFollowedChannels().containsKey(postParentOptional.get().getChannelId())) {
                throw new Exception();
            }
            //check if comment parent belongs to the same post
            if (!comment.isCommentOnPost()) {
                if (!(commentRepository.existsById(comment.getCommentParentId()) &&
                        commentRepository.findById(comment.getCommentParentId()).get()
                                .getPostId().equals(postId)))
                    throw new Exception();
            }

            comment.setPostId(postId);
            commentRepository.save(comment);
            Post post = postParentOptional.get();
            if (!comment.isCommentOnPost()) {
                CommentToComment commentToComment = CommentToComment.builder()
                        .id(comment.getCommentParentId() + comment.getId())
                        .parentComment(commentRepository.findById(comment.getCommentParentId()).get())
                        .childComment(comment)
                        .build();
                commentChildrenHierarchyRepository.save(commentToComment);
            } else {
                PostToComment postToComment = PostToComment.builder()
                        .id(postId + comment.getId())
                        .post(post)
                        .comment(comment)
                        .build();
                postCommentHierarchyRepository.save(postToComment);
            }
            //Comment Id is generated and set by save of commentRepository
            //Update the post and return it to update cache
            if (cacheManager.getCache("postsCache").get(postId) != null) {
                cachingService.updatePostsCache(postId, post);
                System.err.println("in cache");
            }
            if (cacheManager.getCache("popularPostsCache").get(postId) != null)
                cachingService.updatePopularPostsCache(postId, post);
            if (comment.isCommentOnPost()) {
                rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                        "Comment made on your Post",
                        "body: " + comment.getBody() + " by " + comment.getUserNameId(),
                        comment.getUserNameId(),
                        List.of(post.getUserNameId())
                ), MessageQueues.RESPONSE_NOTIFICATIONS);
            } else {
                Comment parent = commentRepository.findById(comment.getCommentParentId()).get();
                rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                        "Comment made on your Comment",
                        "body: " + comment.getBody() + " by " + comment.getUserNameId(),
                        comment.getUserNameId(),
                        List.of(parent.getUserNameId())
                ), MessageQueues.RESPONSE_NOTIFICATIONS);

            }
            return post;
        } catch (Exception e) {

            throw new Exception("Invalid Action");
            //return "Error: Couldn't add comment";
        }
    }

    //    @Cacheable(cacheNames = {"postsCache"},key = "#postId")
//    private Post getPost(String postId){
//        final Optional<Post> postParentOptional = postRepository.findById(postId);
//        if(postParentOptional.isEmpty())
//            return null;
//        return postParentOptional.get();
//    }
    @RabbitListener(queues = "${mq.queues.response.reddit.comment}")
    public void receive(Post response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
