package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.ICommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.app.validation.PostVoteValidation;
import com.ScalableTeam.reddit.config.PopularityConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class UpvotePostService implements ICommand<VotePostForm, String> {
    private final PostRepository postRepository;
    private final UserVotePostRepository userVotePostRepository;
    private final PostVoteRepository postVoteRepository;
    private final PostVoteValidation postVoteValidation;
    private final RabbitMQProducer rabbitMQProducer;
    //    @Value("${popularPostsUpvoteThreshold}")
    //    @Value("${popularPostsCache}")
    //    private String popularPostsCache;
    //    @Value("${postsCache}")
    //    private String postsCache;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    CachingService cachingService;
    @Autowired
    private PopularityConfig popularityConfig;

    @RabbitListener(queues = "${mq.queues.request.reddit.upvotePost}")
    public String execute(VotePostForm votePostForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Vote Post Form={}", commandName, correlationId, votePostForm);
        return execute(votePostForm);
    }

    @Transactional(rollbackFor = {Exception.class}, isolation = Isolation.REPEATABLE_READ)
    @Override
    public String execute(VotePostForm votePostForm) throws Exception {
        log.info("Service::Vote Post Form={}", votePostForm);
        int popularPostsUpvoteThreshold = 1;
        String userNameId = votePostForm.getUserNameId();
        String postId = votePostForm.getPostId();

        postVoteValidation.validatePostVote(userNameId, postId);
        Post post = postRepository.findById(postId).get();
        Long oldUpvotesCount = post.getUpvoteCount(), oldDownvotesCount = post.getDownvoteCount();

        try {
            String responseMessage = userVotePostRepository.upvotePost(userNameId, postId);

            PostVote postVote = postVoteRepository.findById(postId).get();
            Long upvotesCount = postVote.getUpvotes(), downvotesCount = postVote.getDownvotes();
            post.setUpvoteCount(upvotesCount);
            post.setDownvoteCount(downvotesCount);
            postRepository.save(post);

            if (upvotesCount >= popularityConfig.getPostsUpvoteThreshold()) {
                System.err.println("here post");
                cachingService.updatePopularPostsCache(postId, post);
            }
            if (cacheManager.getCache("postsCache").get(postId) != null) {
                cachingService.updatePostsCache(postId, post);
            }
            String result = String.format("User %s %s %s", userNameId, responseMessage, postId);
            rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                            "Upvote Update on one of your posts",
                            result,
                            userNameId,
                            List.of(post.getUserNameId())),
                    MessageQueues.RESPONSE_NOTIFICATIONS
            );
            return result;
        } catch (Exception e) {
            post.setUpvoteCount(oldUpvotesCount);
            post.setDownvoteCount(oldDownvotesCount);
            postRepository.save(post);
            throw e;
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.upvotePost}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}