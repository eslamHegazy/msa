package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class CreatePostService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeneralConfig generalConfig;
//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, Post> hashOperations;
    @Override
    public Post execute(Object postObj) throws Exception {
        log.info(generalConfig.getCommands().get("createPost") + "Service", postObj);
        // TODO: CHECK THE USER IS AUTHENTICATED AND IS SAME USER IN POST
        try {
            Post post = (Post) postObj;
            //Make necessary checks that the user follows this channel
            final Optional<User> postCreatorOptional = userRepository.findById(post.getUserNameId());
            if (postCreatorOptional.isEmpty() ||
                    !postCreatorOptional.get().getFollowedChannels().containsKey(post.getChannelId())) {
                throw new Exception();
            }
            Instant time=Instant.now();
            post.setTime(time);
            postRepository.save(post);
            continueExecuting(post,post.getId());
            return post;
        } catch (Exception e) {
            throw new Exception("Error: Couldn't add post");
//            return "Error: Couldn't add post";
        }
    }
    @CachePut(cacheNames = "postsCache",key="#postId")
    public String continueExecuting(Post post,String postId){
        return post.toString();
    }


}
