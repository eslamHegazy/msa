package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Channel;
import com.ScalableTeam.arango.RedditFollowersEdge;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.RedditFollowersEdgeRepository;
import com.ScalableTeam.reddit.app.repository.vote.RedditFollowRepository;
import com.ScalableTeam.reddit.config.PopularityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class FollowRedditService implements MyCommand {
    private final String serviceName = "followReddit";
    @Autowired
    RedditFollowersEdgeRepository redditFollowersEdgeRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedditFollowRepository redditFollowRepository;
    @Autowired
    private PopularityConfig popularityConfig;
    @Autowired
    private CachingService cachingService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}")
    public String listenToRequestQueue(FollowRedditForm followRedditForm, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Follow Reddit Form={}", commandName, correlationId, followRedditForm);
        return execute(followRedditForm);
    }

    public String execute(Object body) {
        FollowRedditForm request = (FollowRedditForm) body;
        log.info("Service::Follow Reddit Form={}", request);

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
            RedditFollowersEdge redditFollowersEdge = RedditFollowersEdge.builder().channel(reddit.get()).user(actualUser).build();

            redditFollowersEdgeRepository.save(redditFollowersEdge);

            int numfollowers = redditFollowRepository.followReddit(redditId);
            System.err.println("currentFollowers " + numfollowers);
            if (numfollowers >= popularityConfig.getChannelFollowersThreshold()) {
                System.err.println("updatingChannelsCache");
                cachingService.updatePopularChannelsCache(redditId, reddit.get());
            }

            try {
                List admins = List.of(channelRepository.findById(redditId).get().getAdminId());
                System.out.println(admins);
                log.info("Controller - Queue: {}, Command: {}, Payload: {}", "sendNotificationCommand", serviceName, request);
                rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, "sendNotificationCommand", new NotificationSendRequest(
                        "New Channel Follower",
                        userId + " followed your channel " + redditId,
                        userId,
                        admins
                ), MessageQueues.RESPONSE_NOTIFICATIONS);
            } catch (Exception e) {
                System.out.println("no admin to receive notification");
            }

            return "followed reddit " + numfollowers;
        } catch (Exception e) {
            throw e;
        }

    }

    @RabbitListener(queues = "${mq.queues.response.reddit." + serviceName + "}")
    public void receive(String response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }


}


