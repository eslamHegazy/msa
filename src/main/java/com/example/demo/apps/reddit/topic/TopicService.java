package com.example.demo.apps.reddit.topic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class TopicService {
    private ArrayList<Topic> topics= new ArrayList<>( Arrays.asList(
            new Topic("spring","springBoot1","springBoot1Desc"),
            new Topic("spring","springBoot2","springBoot2Desc"),
            new Topic("spring","springBoot3","springBoot3Desc")
    ))  ;

    public ArrayList<Topic> getAllTopics() {
        return topics;
    }
    public Topic getTopic(String id){
        int l=topics.size();
        for (int i = 0; i < l; i++) {
            if(topics.get(i).getId().equals(id))
                return topics.get(i);
        }
        return new Topic("error","error","error");
    }
    public void addTopic(Topic t){
        topics.add(t);
    }
}
