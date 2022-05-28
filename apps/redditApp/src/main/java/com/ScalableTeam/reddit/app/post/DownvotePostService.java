package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.ICommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.app.validation.PostVoteValidation;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ScalableTeam.reddit.app.caching.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class DownvotePostService implements ICommand<VotePostForm, String> {
    private final PostRepository postRepository;
    private final UserVotePostRepository userVotePostRepository;
    private final PostVoteRepository postVoteRepository;
    private final GeneralConfig generalConfig;
    private final PostVoteValidation postVoteValidation;
//    @Value("${popularPostsUpvoteThreshold}")

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CachingService cachingService;

    @RabbitListener(queues = "${mq.queues.request.reddit.downvotePost}")
    public String execute(VotePostForm votePostForm, Message message) throws Exception {
        String indicator = generalConfig.getCommands().get("downvotePost");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Queue Listener::Vote Post Form={}, CorrelationId={}", votePostForm, correlationId);
        return execute(votePostForm);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public String execute(VotePostForm votePostForm) throws Exception {
        int popularPostsUpvoteThreshold = 1;
        String userNameId = votePostForm.getUserNameId();
        String postId = votePostForm.getPostId();
        String indicator = generalConfig.getCommands().get("downvotePost");
        log.info(indicator + "Service::Post Id={}, User Id={}", postId, userNameId);

        postVoteValidation.validatePostVote(userNameId, postId);
        String responseMessage = userVotePostRepository.downvotePost(userNameId, postId);

        PostVote postVote = postVoteRepository.findById(postId).get();
        Long upvotesCount = postVote.getUpvotes(), downvotesCount = postVote.getDownvotes();
        Post post = postRepository.findById(postId).get();
        long previousUpvotes = post.getUpvoteCount();
        post.setUpvoteCount(upvotesCount);
        post.setDownvoteCount(downvotesCount);
        postRepository.save(post);
        if (previousUpvotes == popularPostsUpvoteThreshold && upvotesCount == popularPostsUpvoteThreshold - 1) {
            cachingService.removePreviouslyPopularPost(postId);
        } else {
            cachingService.updatePopularPostsCache(postId, post);
        }
        if (cacheManager.getCache("postsCache").get(postId) != null) {
            cachingService.updatePostsCache(postId, post);
        }
        // todo: integrate notifications
        return String.format("User %s %s %s", userNameId, responseMessage, postId);
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.downvotePost}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get("downvotePost");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }

}