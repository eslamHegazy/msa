package com.example.demo.apps.reddit.readWall;

import com.example.demo.apps.reddit.entity.Post;
import com.example.demo.apps.reddit.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReadWallController {
    @Autowired
    private ReadWallService readWallService;
    @RequestMapping("/feed/{userNameId}")
    private Post[] createPost(@PathVariable String userNameId){
        return readWallService.execute(userNameId);
    }
}
