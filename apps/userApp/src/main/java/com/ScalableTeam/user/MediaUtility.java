package com.ScalableTeam.user;

import com.ScalableTeam.models.media.RemovePhotoBody;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MediaUtility {
    private final RabbitTemplate rabbitTemplate;
    public void deleteProfilePhoto(String fileUrl){
        rabbitTemplate.convertAndSend("mediaApp", new RemovePhotoBody(fileUrl), message -> {
            message.getMessageProperties().setHeader("command", "removePhotoCommand");
            return message;
        });
    }
}
