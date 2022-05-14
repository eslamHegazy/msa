package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.reddit.app.createPost.CreatePostService;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FollowRedditController {
    @Autowired
    private FollowRedditService followRedditService;
    @RequestMapping(method = RequestMethod.POST,value = "/followReddit")
    private String createPost(@RequestBody Object body){
        //body:{channelId, userId}
        return followRedditService.execute(body);
    }
}


