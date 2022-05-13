package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.reddit.app.entity.Comment;
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
    @Value("#{${commands}}")
    private Map<String, String> commands;
    @RequestMapping(method = RequestMethod.POST,value="/comments")
    private String comment(@RequestBody Comment comment) throws Exception {
        log.info(commands.get("comment") + "Controller", comment);
        return commentService.execute(comment);
    }
}
