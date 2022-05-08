package com.example.demo.apps.reddit.readWall;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@ComponentScan("com.example.demo")
@Service
public class ReadWallService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Post[] execute(Object userNameIdString) {

        try{

            String userNameId=(String)userNameIdString;
            final Optional<User> userOptional = userRepository.findById(userNameId);

            if (userOptional.isEmpty()) {
                return null;
            }
            User user=userOptional.get();
            //FIND NEW TOP 25 POSTS WHERE CHANNEL IN FOLLOWED CHANNELS
            //FIND NEW TOP OF 25 POSTS WHERE USER IN FOLLOWED USERS
            Date earliestTime=user.getEarliestTime()==null?Date.from(Instant.now()):user.getEarliestTime();
            Date latestTime=user.getLatestTime()==null?Date.from(Instant.now()):user.getLatestTime();

            Post[]feedFromChannels=postRepository.getPostsByTimeAndChannel(earliestTime,latestTime, user.getFollowedChannels());
            Post[]feedFromUsers=(postRepository.getPostsByTimeAndUser(earliestTime,latestTime,user.getFollowedUsers()));
            Post[]feedTotal=new Post[feedFromUsers.length+feedFromChannels.length];
            for (int i = 0; i < feedFromChannels.length ; i++) {
                feedTotal[i]=feedFromChannels[i];
                Instant time=(feedTotal[i].getTime());
                if(time==null)
                    time=Instant.now();
                Date date=Date.from(time);
                if(earliestTime.compareTo(date)<0){
                   earliestTime=date;
                }
                if(latestTime.compareTo(date)>0){
                   latestTime=date;
                }
            }
            for (int i = 0; i < feedFromUsers.length ; i++) {
                feedTotal[i+ feedFromChannels.length]=feedFromUsers[i];
                Instant time=(feedTotal[i].getTime());
                if(time==null)
                    time=Instant.now();
                Date date=Date.from(time);
                if(earliestTime.compareTo(date)<0){
                    earliestTime=date;
                }
                if(latestTime.compareTo(date)>0){
                    latestTime=date;
                }
            }
            user.setEarliestTime(earliestTime);
            user.setLatestTime(latestTime);
            userRepository.save(user);

            return feedTotal ;
        } catch (Exception e) {

            return null;
        }

    }

}
