package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.FollowRedditForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class FollowRedditService implements MyCommand {
    @Autowired
    private final ChannelRepository channelRepository;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public FollowRedditService(ChannelRepository channelRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.userRepository=userRepository;
    }


    public String execute(Object body){


        FollowRedditForm request = (FollowRedditForm) body;

        String  userId = request.getUserId();
        String redditId= request.getRedditId();

        try{
        Optional<Channel> reddit  = channelRepository.findById(redditId);
        if (!reddit.isPresent()){
            throw new IllegalStateException("Reddit not found in DB!");
        }

        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new IllegalStateException("User not found in DB!");

        }


        if(reddit.get().getBannedUsers()!=null&&reddit.get().getBannedUsers().containsKey(userId)){
            return "User "+userId + " banned from this channel " + redditId;
        }

        HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
        follow.put(redditId, true);
        userRepository.updateFollowedChannelsWithID(userId, follow);

        System.out.println(userId + "    "+redditId);
        return "Channel followed successfully";
        }catch(Exception e){
            return e.getMessage();
        }

    }
}


