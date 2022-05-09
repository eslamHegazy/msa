package com.ScalableTeam.reddit.app.createPost;

import com.ScalableTeam.reddit.app.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreatePostController {
    @Autowired
    private CreatePostService createPostService;
    @RequestMapping(method = RequestMethod.POST,value = "/posts")
    private String createPost(@RequestBody Post post){
        return createPostService.execute(post);
    }
}
