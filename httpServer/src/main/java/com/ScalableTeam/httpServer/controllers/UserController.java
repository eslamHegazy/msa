package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.models.user.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    public static final String QUEUE = "user-app";
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/userTest")
    public String testUser() {
        Object o = rabbitTemplate.convertSendAndReceiveAsType(QUEUE, "", message -> {
            message.getMessageProperties().setHeader("command", "userTestCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
        return "Hello Maria";
    }

    @DeleteMapping("/deleteAccount")
    public DeleteAccountResponse deleteUser(@RequestBody DeleteAccountBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "deleteAccountCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/blockUser")
    public BlockedUserResponse blockUser(@RequestBody BlockedUserBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "blockUserCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/reportUser")
    public ReportedUserResponse reportUser(@RequestBody ReportUserBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "reportUserCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/followUser")
    public FollowUserResponse FollowUser(@RequestBody FollowUserBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "followUserCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/unfollowUser")
    public UnFollowUserResponse UnfollowUser(@RequestBody UnFollowUserBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "unFollowUserCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/editProfile")
    public EditProfileResponse editProfile(@RequestBody EditProfileBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "editProfileCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }

    @PostMapping("/updatePhoto")
    public UpdatePhotoResponse updatePhoto(@RequestBody UpdatePhotoBody body) {
        return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
            message.getMessageProperties().setHeader("command", "updatePhotoCommand");
            return message;
        }, new ParameterizedTypeReference<>() {
        });
    }
}
