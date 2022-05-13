package com.ScalableTeam.reddit.app.readWall;

import com.ScalableTeam.reddit.app.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class ReadWallController {
    @Autowired
    private ReadWallService readWallService;
    @Value("#{${commands}}")
    private Map<String, String> commands;
    @RequestMapping("/feed/{userNameId}")
    private Post[] createPost(@PathVariable String userNameId) throws Exception {
        log.info(commands.get("readWall") + "Controller", userNameId);
        return readWallService.execute(userNameId);
    }
}
