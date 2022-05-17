package com.ScalableTeam.reddit.app.createPost;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@Slf4j
public class CreatePostController {

    @Autowired
    private CreatePostService createPostService;
    @Autowired
    private GetPostService getPostService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST,value = "/posts")
    private Post createPost(@RequestBody Post post) throws Exception {
        log.info(generalConfig.getCommands().get("createPost") + "Controller", post);
        return createPostService.execute(post);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/posts/{postId}")
    private Post createPost(@PathVariable String postId) throws Exception {
        log.info(generalConfig.getCommands().get("getPost") + "Controller", postId);
        return getPostService.execute(postId);
    }

}
