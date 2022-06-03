package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.user.LoginBody;
import com.ScalableTeam.models.user.LoginResponse;
import com.ScalableTeam.models.user.SignUpBody;
import com.ScalableTeam.models.user.SignUpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthenticationController {

    public static final String QUEUE = "user-app";

    private final RabbitTemplate rabbitTemplate;
    private final CommandsMapper commandsMapper;

    @PostMapping("/signUp")
    public SignUpResponse signUp(@RequestBody SignUpBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", commandsMapper.getUser().get("signUp"));
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", commandsMapper.getUser().get("login"));
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }
}
