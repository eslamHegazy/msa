package com.ScalableTeam.reddit.app.createPost;

import com.ScalableTeam.reddit.app.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class CreatePostController {
    @Autowired
    private CreatePostService createPostService;
    @Value("#{${commands}}")
    private Map<String, String> commands;
    @RequestMapping(method = RequestMethod.POST,value = "/posts")
    private String createPost(@RequestBody Post post) throws Exception {
        log.info(commands.get("createPost") + "Controller", post);
        return createPostService.execute(post);
    }
}
