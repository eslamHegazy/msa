
package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.BanUserForm;
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
    public String execute(Object body) throws Exception {

try {

    BanUserForm request = (BanUserForm) body;
    String modId = request.getModId();
    String requestedBanUserId =request.getRequestedBanUserId();
    String redditId = request.getRedditId();

    Optional<Channel> reddit = channelRepository.findById(redditId);
    if (!reddit.isPresent()) {
        throw new IllegalStateException("Reddit not found in DB!");
    }

    Optional<User> user = userRepository.findById(requestedBanUserId);
    if (!user.isPresent()) {
        throw new IllegalStateException("User not found in DB!");
    }

    if (!reddit.get().getModerators().containsKey(modId)) {
        return "User " + modId + " is not a mod for channel " + redditId;
    }

    User toban = user.get();
    if(toban.getFollowedChannels().isEmpty() || !toban.getFollowedChannels().containsKey(redditId)){
        return "User " + requestedBanUserId + " is not following channel " + redditId;
    }

    HashMap<String, Boolean> ban = new HashMap<String, Boolean>();
    ban.put(requestedBanUserId, true);
    channelRepository.updateBannedUsersWithID(redditId, ban);

    return "user " + requestedBanUserId + " banned successfully from channel " + redditId;
}
catch(Exception e){
    return e.getMessage();
}
    }
}
