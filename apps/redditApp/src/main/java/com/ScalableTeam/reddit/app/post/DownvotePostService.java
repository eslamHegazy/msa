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
public class DownvotePostService implements ICommand<VotePostForm, String> {
    private final PostRepository postRepository;
    private final UserVotePostRepository userVotePostRepository;
    private final PostVoteRepository postVoteRepository;
    private final PostVoteValidation postVoteValidation;
//    @Value("${popularPostsUpvoteThreshold}")

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CachingService cachingService;
    @Autowired
    private PopularityConfig popularityConfig;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${mq.queues.request.reddit.downvotePost}")
    public String execute(VotePostForm votePostForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Downvote Post Form={}", commandName, correlationId, votePostForm);
        return execute(votePostForm);
    }

    @Transactional(rollbackFor = {Exception.class}, isolation = Isolation.REPEATABLE_READ)
    @Override
    public String execute(VotePostForm votePostForm) throws Exception {
        log.info("Service::Downvote Post Form={}", votePostForm);
        int popularPostsUpvoteThreshold = 1;
        String userNameId = votePostForm.getUserNameId();
        String postId = votePostForm.getPostId();

        postVoteValidation.validatePostVote(userNameId, postId);
        Post post = postRepository.findById(postId).get();
        Long oldUpvotesCount = post.getUpvoteCount(), oldDownvotesCount = post.getDownvoteCount();

        try {
            String responseMessage = userVotePostRepository.downvotePost(userNameId, postId);

            PostVote postVote = postVoteRepository.findById(postId).get();
            Long upvotesCount = postVote.getUpvotes(), downvotesCount = postVote.getDownvotes();
            long previousUpvotes = post.getUpvoteCount();
            post.setUpvoteCount(upvotesCount);
            post.setDownvoteCount(downvotesCount);
            postRepository.save(post);
            if (previousUpvotes == popularityConfig.getPostsUpvoteThreshold() && upvotesCount == popularityConfig.getPostsUpvoteThreshold() - 1) {
                cachingService.removePreviouslyPopularPost(postId);
            } else {
                cachingService.updatePopularPostsCache(postId, post);
            }
            if (cacheManager.getCache("postsCache").get(postId) != null) {
                cachingService.updatePostsCache(postId, post);
            }
            String result = String.format("User %s %s %s", userNameId, responseMessage, postId);
            rabbitMQProducer.publishSynchronous(MessageQueues.NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                    "Downvote Update on one of your posts",
                    result,
                    userNameId,
                    List.of(post.getUserNameId())
            ));
            return result;
        } catch (Exception e) {
            post.setUpvoteCount(oldUpvotesCount);
            post.setDownvoteCount(oldDownvotesCount);
            postRepository.save(post);
            throw e;
        }
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.downvotePost}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}