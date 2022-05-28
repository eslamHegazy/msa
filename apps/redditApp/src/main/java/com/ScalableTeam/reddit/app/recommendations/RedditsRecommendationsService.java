package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class RedditsRecommendationsService implements MyCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private Config config;

    private final String serviceName = "redditsRecommendations";

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}", returnExceptions = "true")
    public String[] listenToRequestQueue(String userId, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get(serviceName);
        log.info(indicator + "Service::View Reports, CorrelationId={}", correlationId);
        return execute(userId);
    }

    @Override
    public String[] execute(Object body) throws Exception {
        //    1. get all/10 random/first entries in user's followedUsers
//    2. get all their followed reddits (put them together)
//    return most frequently occuring reddits (or first 5 if none repeat)
        log.info(generalConfig.getCommands().get("redditsRecommendations") + "Service", body);

        try {
            String userId = (String) body;
            Optional<User> userO = userRepository.findById(userId);

            if (!userO.isPresent()) {
                throw new IllegalStateException("User " + userId + "not found in DataBase");
            }


            User user = userO.get();
            if (user.getFollowedUsers() == null) {
                return new String[0];
            }

            HashMap<String, Boolean> followedUsers = user.getFollowedUsers();

            HashMap<String, Integer> frequencies = new HashMap<String, Integer>();

            for (String uId : followedUsers.keySet()) {
                Optional<User> u = userRepository.findById(uId);
                if (u.isPresent()) {
                    User u2 = u.get();
                    if (u2.getFollowedChannels() != null) {
                        Set<String> rList = u2.getFollowedChannels().keySet();
                        for (String r : rList) {

                            if (frequencies.containsKey(r)) {
                                frequencies.replace(r, frequencies.get(r) + 1);
                            } else {
                                frequencies.put(r, 1);
                            }
                        }

                    }

                }

            }

            int counter = 5;
            String[] result = new String[counter];
            Set set2 = frequencies.entrySet();
            Iterator iterator2 = set2.iterator();
            while (iterator2.hasNext() && counter > 0) {
                Map.Entry me2 = (Map.Entry) iterator2.next();
                result[counter - 1] = (String) me2.getKey();
                counter--;
            }
            return result;

        } catch (Exception e) {
            throw e;
        }

    }

}
