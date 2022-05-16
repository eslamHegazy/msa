package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private CreatePostService createPostService;
    @Autowired
    private UpvotePostService upvotePostService;
    @Autowired
    private DownvotePostService downvotePostService;
    @Autowired
    private GeneralConfig generalConfig;

    @PostMapping
    public String createPost(@RequestBody Post post) throws Exception {
        String indicator = generalConfig.getCommands().get("createPost");
        log.info(indicator + "Controller", post);
        return createPostService.execute(post);
    }

    @PostMapping("upvote/{id}")
    public String upvotePost(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String indicator = generalConfig.getCommands().get("upvotePost");
        log.info(indicator + "Controller::Post Id={}, User Id={}", id, userNameId);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userNameId", userNameId);
        attributes.put("postId", id);
        return upvotePostService.execute(attributes);
    }

    @PostMapping("downvote/{id}")
    public String downvotePost(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String indicator = generalConfig.getCommands().get("downvotePost");
        log.info(indicator + "Controller::Post Id={}, User Id={}", id, userNameId);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userNameId", userNameId);
        attributes.put("postId", id);
        return downvotePostService.execute(attributes);
    }
}
