package com.ScalableTeam.reddit.app.createPost;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
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
    @Value("#{${commands}}")
    private Map<String, String> commands;
    @Override
    public String execute(Object postObj) throws Exception {
        log.info(commands.get("createPost") + "Service", postObj);
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
            return "Post uploaded successfully";
        } catch (Exception e) {
            throw new Exception("Error: Couldn't add post");
//            return "Error: Couldn't add post";
        }


    }


}
