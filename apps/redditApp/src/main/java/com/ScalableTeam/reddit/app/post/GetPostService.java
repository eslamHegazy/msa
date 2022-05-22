package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class GetPostService implements MyCommand {
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
    public Post execute(Object postId) throws Exception {
        log.info(generalConfig.getCommands().get("getPost") + "Service", postId);
        // TODO: CHECK THE USER IS AUTHENTICATED AND IS SAME USER IN POST
        try {
          String postIdString=(String)postId;
            //Make necessary checks that the user follows this channel
            return continueExecuting(postIdString);
        } catch (Exception e) {
            throw new Exception("Error: Couldn't add post");
//            return "Error: Couldn't add post";
        }
    }
    @Cacheable(cacheNames = "postsCache",key="#postId")
    public Post continueExecuting(String postId){
        return postRepository.findById(postId).get();
    }
}