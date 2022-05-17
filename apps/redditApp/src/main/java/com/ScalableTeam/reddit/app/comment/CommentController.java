package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
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
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST,value="/comments")
    private Post comment(@RequestBody Comment comment) throws Exception {
        log.info(generalConfig.getCommands().get("comment") + "Controller", comment);
        return commentService.execute(comment);
    }
}
