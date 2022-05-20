package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class RedditsRecommendationsService implements MyCommand {

    @Autowired
    private final UserRepository userRepository;

    public RedditsRecommendationsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private GeneralConfig generalConfig;
    @Override
    public Post[] execute(Object body) throws Exception {
        //    1. get all/10 random/first entries in user's followedUsers
//    2. get all their followed reddits (put them together)
//    return most frequently occuring reddits (or first 5 if none repeat)
        log.info(generalConfig.getCommands().get("redditsRecommendations") + "Service", body);

        try{
            String userId = (String) body;

            Optional<User> userO = userRepository.findById(userId);
            if(!userO.isPresent()){
                throw new IllegalStateException("User " + userId + "not found in DataBase");
            }

            User user = userO.get();
            if (user.getFollowedUsers()==null){
                return new Post[1];
            }
            HashMap<String,Boolean> followedUsers = user.getFollowedUsers();
            Post [] choices = new Post[100];

        }catch (Exception e){
            throw e;
        }

        return null;
    }

}
