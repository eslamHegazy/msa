package com.ScalableTeam.reddit.app.recommendations;


import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
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
public class RecommendationsBasedOnFollowersService implements MyCommand {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RecommendationsPerChannel recommendationsPerChannel;
    private final String serviceName = "recommendationsBasedOnFollowersService";
    @Autowired
    private Config config;

    @RabbitListener(queues = "${mq.queues.request.reddit." + serviceName + "}", returnExceptions = "true")
    public  ArrayList<String> listenToRequestQueue(String userId, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Recommendations Based On Followers Form={}", commandName, correlationId, userId);
        return execute(userId);
    }

    @Override
    public ArrayList<String> execute(Object body) throws Exception {
        //    1. get all/10 random/first entries in user's followedUsers
//    2. get all their followed reddits (put them together)
//    return most frequently occuring reddits (or first 5 if none repeat)
        try {
            String userId = (String) body;
            log.info("Service::Recommendations Based On Followers Form={}", userId);
            User user = userRepository.findById(userId).get();
            String [] followIds = user.getFollowedChannels().keySet().toArray(String[]::new);
            HashMap<String, Integer> frequencies = new HashMap<>();
            for(int i=0;i<3;i++){
                HashMap<String, Integer> temp = String2Hash(recommendationsPerChannel.execute(followIds[i]));
                for (String k : temp.keySet()){
                    if (frequencies.containsKey(k)){
                        frequencies.replace(k,frequencies.get(k)+temp.get(k));
                    }else {
                        frequencies.put(k, temp.get(k));
                    }
                }
            }

            Collection<Integer> freq_vals = frequencies.values();
            List<Integer> freqlist = new ArrayList<Integer>();
            freqlist.addAll(freq_vals);
            Collections.sort(freqlist);
            System.out.println(freqlist.toString());

            ArrayList<String> result = new ArrayList<String>();

            while(result.size()<5){
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

            System.out.println(result);

            return result;

        } catch (Exception e) {
            throw e;
        }

    }

    private HashMap<String, Integer> String2Hash(String s){
        HashMap<String,Integer> res = new HashMap<>();
        s = s.substring(1,s.length()-1);
        String [] items = s.split(",");
        for (String it :items){
            String [] entry = it.split("=");
            res.put(entry[0],Integer.parseInt(entry[1]));
        }
        return res;
    }
}
