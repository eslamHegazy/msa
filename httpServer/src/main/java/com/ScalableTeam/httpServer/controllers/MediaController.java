package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.models.media.UploadPhotoBody;
import com.ScalableTeam.models.media.UploadPhotoResponse;
import com.ScalableTeam.models.user.BlockedUserBody;
import com.ScalableTeam.models.user.BlockedUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("media")
@AllArgsConstructor
public class MediaController {
    public static final String QUEUE = "mediaApp";
    private final RabbitTemplate rabbitTemplate;
    @PostMapping("/uploadPhoto")
    public UploadPhotoResponse uploadPhoto(@RequestBody MultipartFile files){
        try{
            UploadPhotoBody body = new UploadPhotoBody(files.getBytes(), files.getContentType());
            return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
                message.getMessageProperties().setHeader("command", "uploadPhotoCommand");
                return message;
            }, new ParameterizedTypeReference<>() {
            });
        }
        catch (Exception e){
            return new UploadPhotoResponse("Invalid Input", false);
        }

    }
}
