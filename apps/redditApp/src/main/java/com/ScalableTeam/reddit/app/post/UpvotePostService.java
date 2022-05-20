package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.PostVote;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.PostVoteRepository;
import com.ScalableTeam.reddit.app.repository.UserVotePostRepository;
import com.ScalableTeam.reddit.app.validation.PostVoteValidation;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
@AllArgsConstructor
public class UpvotePostService implements MyCommand {
    private final PostRepository postRepository;
    private final UserVotePostRepository userVotePostRepository;
    private final PostVoteRepository postVoteRepository;
    private final GeneralConfig generalConfig;
    private final PostVoteValidation postVoteValidation;
//    @Value("${popularPostsUpvoteThreshold}")
//    @Value("${popularPostsCache}")
//    private String popularPostsCache;
//    @Value("${postsCache}")
//    private String postsCache;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    CachingService cachingService;

    @Transactional
    public String execute(Object obj) throws Exception {
        int popularPostsUpvoteThreshold=1;
        Map<String, Object> attributes = (Map<String, Object>) obj;
        String userNameId = (String) attributes.get("userNameId");
        String postId = (String) attributes.get("postId");
        String indicator = generalConfig.getCommands().get("upvotePost");
        log.info(indicator + "Service::Post Id={}, User Id={}", postId, userNameId);

        postVoteValidation.validatePostVote(userNameId, postId);
        String responseMessage = userVotePostRepository.upvotePost(userNameId, postId);

        PostVote postVote = postVoteRepository.findById(postId).get();
        Long upvotesCount = postVote.getUpvotes(), downvotesCount = postVote.getDownvotes();
        Post post = postRepository.findById(postId).get();
        post.setUpvoteCount(upvotesCount);
        post.setDownvoteCount(downvotesCount);
        postRepository.save(post);
        if(upvotesCount>= popularPostsUpvoteThreshold){
            System.err.println("here post");
            cachingService.updatePopularPostsCache(postId,post);
        }
        if (cacheManager.getCache("postsCache").get(postId) != null) {
            cachingService.updatePostsCache(postId, post);
        }



        // todo: integrate notifications
        return String.format("User %s %s %s", userNameId, responseMessage, postId);
    }


}
