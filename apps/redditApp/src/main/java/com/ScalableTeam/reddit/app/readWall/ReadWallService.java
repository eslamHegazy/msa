package com.ScalableTeam.reddit.app.readWall;

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
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ReadWallService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("#{${commands}}")
    private Map<String, String> commands;
    @Override
    public Post[] execute(Object userNameIdString) throws Exception {
        log.info(commands.get("readWall") + "Service", userNameIdString);
        try{

            String userNameId=(String)userNameIdString;
            final Optional<User> userOptional = userRepository.findById(userNameId);

            if (userOptional.isEmpty()) {
                throw new Exception();
            }
            User user=userOptional.get();
            //FIND NEW TOP 25 POSTS WHERE CHANNEL IN FOLLOWED CHANNELS
            //FIND NEW TOP OF 25 POSTS WHERE USER IN FOLLOWED USERS
//            Date earliestTime=user.getEarliestTime()==null?Date.from(Instant.now()):user.getEarliestTime();
//            Date latestTime=user.getLatestTime()==null?Date.from(Instant.now()):user.getLatestTime();
            String newLatestReadPostId=user.getLatestReadPostId()==null?"":user.getLatestReadPostId();
            Post[]feedFromChannels=postRepository.getPostsByTimeAndChannel(newLatestReadPostId, user.getFollowedChannels());
            Post[]feedFromUsers=(postRepository.getPostsByTimeAndUser(newLatestReadPostId,user.getFollowedUsers()));
            Post[]feedTotal=new Post[feedFromUsers.length+feedFromChannels.length];

            for (int i = 0; i < feedFromChannels.length ; i++) {
                feedTotal[i]=feedFromChannels[i];
//                Instant time=(feedTotal[i].getTime());
//                if(time==null)
//                    time=Instant.now();
//                Date date=Date.from(time);
//                if(earliestTime.compareTo(date)<0){
//                   earliestTime=date;
//                }
//                if(latestTime.compareTo(date)>0){
//                   latestTime=date;
//                }
                if(feedTotal[i].getId().compareTo(newLatestReadPostId)>0){
                    newLatestReadPostId=feedTotal[i].getId();
                }
            }
            for (int i = 0; i < feedFromUsers.length ; i++) {
                feedTotal[i+ feedFromChannels.length]=feedFromUsers[i];
//                Instant time=(feedTotal[i].getTime());
//                if(time==null)
//                    time=Instant.now();
//                Date date=Date.from(time);
//                if(earliestTime.compareTo(date)<0){
//                    earliestTime=date;
//                }
//                if(latestTime.compareTo(date)>0){
//                    latestTime=date;
//                }
                if(feedTotal[i+ feedFromChannels.length].getId().compareTo(newLatestReadPostId)>0){
                    newLatestReadPostId=feedTotal[i].getId();
                }
            }
//            user.setEarliestTime(earliestTime);
//            user.setLatestTime(latestTime);
            user.setLatestReadPostId(newLatestReadPostId);
            userRepository.save(user);

            return feedTotal ;
        } catch (Exception e) {

            throw new Exception("Exception When getting the feed");
        }

    }

}
