package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Optional;

public class UnfollowRedditService implements MyCommand {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private RedditFollowRepository redditFollowRepository;

    private final String serviceName = "unfollowReddit";

    public String execute(Object body) {


        FollowRedditForm request = (FollowRedditForm) body;

        String userId = request.getUserId();
        String redditId = request.getRedditId();

        try {
            Optional<Channel> reddit = channelRepository.findById(redditId);
            if (!reddit.isPresent()) {
                throw new IllegalStateException("Reddit not found in DB!");
            }

            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                throw new IllegalStateException("User not found in DB!");

            }


            if (reddit.get().getBannedUsers() != null && reddit.get().getBannedUsers().containsKey(userId)) {
                return "User " + userId + " banned from this channel " + redditId;
            }

            User actualUser = user.get();


            HashMap<String, Boolean> follow = new HashMap<String, Boolean>();
            follow.put(redditId, true);
            if (actualUser.getFollowedChannels() == null) {
                actualUser.setFollowedChannels(follow);
                userRepository.save(actualUser);
            } else {
                if (actualUser.getFollowedChannels().containsKey(redditId)) {
                    return "user already following channel";
                }
                userRepository.updateFollowedChannelsWithID(userId, follow);
            }
            int numfollowers = redditFollowRepository.followReddit(redditId);

            return "unfollowed reddit " + numfollowers;
        } catch (Exception e) {
            throw e;
        }

    }


}
