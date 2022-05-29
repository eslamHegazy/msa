package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.models.user.DeleteAccountBody;
import com.ScalableTeam.models.user.DeleteAccountResponse;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    public static final String QUEUE = "user-app";
    private final RabbitTemplate rabbitTemplate;

    @DeleteMapping("/deleteAccount")
    public DeleteAccountResponse deleteUser(@RequestBody DeleteAccountBody body){
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "deleteAccountCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }
}
