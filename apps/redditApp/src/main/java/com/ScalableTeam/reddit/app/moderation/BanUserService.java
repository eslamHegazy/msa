
package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class BanUserService implements MyCommand {
    @Autowired
private final ChannelRepository channelRepository;
@Autowired
private final UserRepository userRepository;

@Autowired
BanUserService(ChannelRepository channelRepository, UserRepository userRepository){
    this.channelRepository = channelRepository;
    this.userRepository=userRepository;
}


    @Override
    public Object execute(Object body) throws Exception {

        HashMap<String,String> requestBody = (HashMap<String,String>) body;
            String modId = requestBody.get("userId");
            String requestedBanUserId = requestBody.get("requestedBanUserId");
            String redditId = requestBody.get("redditId");

        Optional<Channel> reddit  = channelRepository.findById(redditId);
        if (!reddit.isPresent()){
            throw new IllegalStateException("Reddit not found in DB!");
        }

        Optional<User> user = userRepository.findById(requestedBanUserId);
        if(!user.isPresent()){
            throw new IllegalStateException("User not found in DB!");
        }

        if(!reddit.get().getModerators().containsKey(modId)){
            return "User " + modId + " is not a mod for channel "+ redditId;
        }

        HashMap<String, Boolean> ban = new HashMap<String, Boolean>();
        ban.put(requestedBanUserId,true);
        channelRepository.updateBannedUsersWithID(redditId,ban);

        return "user " + requestedBanUserId + " banned successfully from channel "+ redditId;
    }
}
