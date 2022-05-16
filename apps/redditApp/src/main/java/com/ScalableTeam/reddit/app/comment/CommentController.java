package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.post.DownvotePostService;
import com.ScalableTeam.reddit.app.post.UpvotePostService;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpvoteCommentService upvoteCommentService;
    @Autowired
    private DownvoteCommentService downvoteCommentService;
    @Autowired
    private GeneralConfig generalConfig;

    @PostMapping
    private String comment(@RequestBody Comment comment) throws Exception {
        log.info(generalConfig.getCommands().get("comment") + "Controller", comment);
        return commentService.execute(comment);
    }

    @PostMapping("upvote/{id}")
    public String upvoteComment(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String indicator = generalConfig.getCommands().get("upvoteComment");
        log.info(indicator + "Controller::Comment Id={}, User Id={}", id, userNameId);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userNameId", userNameId);
        attributes.put("commentId", id);
        return upvoteCommentService.execute(attributes);
    }

    @PostMapping("downvote/{id}")
    public String downvoteComment(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String indicator = generalConfig.getCommands().get("downvoteComment");
        log.info(indicator + "Controller::Comment Id={}, User Id={}", id, userNameId);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userNameId", userNameId);
        attributes.put("commentId", id);
        return downvoteCommentService.execute(attributes);
    }
}
