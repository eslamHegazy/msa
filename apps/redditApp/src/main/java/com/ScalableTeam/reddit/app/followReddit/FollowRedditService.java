package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class FollowRedditService implements MyCommand {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private RedditFollowRepository redditFollowRepository;

    private final String serviceName = "followReddit";

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}")
    public String listenToRequestQueue(FollowRedditForm followRedditForm, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get(serviceName);
        log.info(indicator + "Service::" + serviceName + ", CorrelationId={}", correlationId);
        return execute(followRedditForm);
    }

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
            String msg = redditFollowRepository.followReddit(redditId);

            return msg;
        } catch (Exception e) {
            throw e;
        }

    }

    @RabbitListener(queues = "${mq.queues.response.reddit." + serviceName + "}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get(serviceName);
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }


}


