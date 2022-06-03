package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.*;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class RedditsRecommendationsService implements MyCommand {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Config config;
    private final String serviceName = "redditsRecommendations";

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}", returnExceptions = "true")
    public Object listenToRequestQueue(String userId, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Reddit Recommendation Form={}", commandName, correlationId, userId);
        return execute(userId);
    }

    @Override
    public Object execute(Object body) throws Exception {
        //    1. get all/10 random/first entries in user's followedUsers
//    2. get all their followed reddits (put them together)
//    return most frequently occuring reddits (or first 5 if none repeat)

        try {
            String userId = (String) body;
            log.info("Service::Reddit Recommendation Form={}", userId);
            Optional<User> userO = userRepository.findById(userId);

            if (!userO.isPresent()) {
                throw new IllegalStateException("User " + userId + "not found in DataBase");
            }


            User user = userO.get();
            if (user.getFollowedUsers() == null) {
                return "no recommendations";
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

            System.out.println(frequencies);
            Collection<Integer> freq_vals = frequencies.values();
            List<Integer> freqlist = new ArrayList<Integer>();
            freqlist.addAll(freq_vals);
            Collections.sort(freqlist);
            System.out.println(freqlist.toString());

            ArrayList<String> result = new ArrayList<String>();

            while(result.size()<5 && freqlist.size()>0){
                boolean found = false;
                Iterator it = frequencies.entrySet().iterator();
                System.out.println(result);
                while (it.hasNext()){
                    HashMap.Entry item = (Map.Entry) it.next();
                    if(item.getValue()==freqlist.get(freqlist.size()-1)){
                        result.add((String) item.getKey());
                        found=true;
                        break;
                    }

                }
                frequencies.remove(result.get(result.size()-1));
                if(!found){
                    freqlist.remove(freqlist.size()-1);
                }
            }



            return result.toString();

        } catch (Exception e) {
            throw e;
        }

    }

}
