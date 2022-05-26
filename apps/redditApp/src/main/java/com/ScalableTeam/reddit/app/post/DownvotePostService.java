package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.vote.PostVote;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.vote.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.vote.UserVotePostRepository;
import com.ScalableTeam.reddit.app.requestForms.VotePostForm;
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

import java.util.HashMap;
import java.util.Map;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class DownvotePostService implements MyCommand {
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
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("form", votePostForm);
        attributes.put("message", message);
        return (String) execute(attributes);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Object execute(Object obj) throws Exception {
        int popularPostsUpvoteThreshold = 1;
        Map<String, Object> attributes = (Map<String, Object>) obj;
        VotePostForm votePostForm = (VotePostForm) attributes.get("form");
        Message message = (Message) attributes.get("message");

        String userNameId = votePostForm.getUserNameId();
        String postId = votePostForm.getPostId();
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("downvotePost");
        log.info(indicator + "Service::Post Id={}, User Id={}, correlationId={}", postId, userNameId, correlationId);

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