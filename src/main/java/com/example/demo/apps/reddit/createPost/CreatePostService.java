package com.example.demo.apps.reddit.createPost;

import com.arangodb.springframework.core.ArangoOperations;
import com.example.demo.MyCommand;
import com.example.demo.apps.reddit.entity.Post;
import com.example.demo.apps.reddit.entity.User;
import com.example.demo.apps.reddit.repository.PostRepository;
import com.example.demo.apps.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@ComponentScan("com.example.demo")
@Service
public class CreatePostService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String execute(Object postObj) {
        // TODO: CHECK THE USER IS AUTHENTICATED AND IS SAME USER IN POST
        try {
            Post post = (Post) postObj;
            //Make necessary checks that the user follows this channel
            final Optional<User> postCreatorOptional = userRepository.findById(post.getUserNameId());
            if (postCreatorOptional.isEmpty() ||
                    !postCreatorOptional.get().getFollowedChannels().containsKey(post.getChannelId())) {
                return "invalid Action";
            }
            Instant time=Instant.now();
            post.setTime(time);
            postRepository.save(post);
            return "Post uploaded successfully";
        } catch (Exception e) {
            return "Error: Couldn't add post";
        }


    }


}
