package com.ScalableTeam.reddit.app.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String sayHi(){
        return "Hi";
    }
}
