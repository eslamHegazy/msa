package com.ScalableTeam.reddit.app.readWall;

import com.ScalableTeam.reddit.app.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReadWallController {
    @Autowired
    private ReadWallService readWallService;
    @RequestMapping("/feed/{userNameId}")
    private Post[] createPost(@PathVariable String userNameId) throws Exception {
        return readWallService.execute(userNameId);
    }
}
