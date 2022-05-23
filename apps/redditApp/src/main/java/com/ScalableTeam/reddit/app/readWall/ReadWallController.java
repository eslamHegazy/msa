package com.ScalableTeam.reddit.app.readWall;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
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
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping("/feed/{userNameId}")
    private String readWall(@PathVariable String userNameId) throws Exception {
        log.info(generalConfig.getCommands().get("readWall") + "Controller", userNameId);
        return readWallService.execute(userNameId);
    }
}
