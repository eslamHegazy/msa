package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.models.media.DownloadPhotoBody;
import com.ScalableTeam.models.media.DownloadPhotoResponse;
import com.ScalableTeam.models.media.UploadPhotoBody;
import com.ScalableTeam.models.media.UploadPhotoResponse;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("media")
@AllArgsConstructor
public class MediaController {
    public static final String QUEUE = "mediaApp";
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/uploadPhoto")
    public UploadPhotoResponse uploadPhoto(@RequestBody MultipartFile files) {
        try {
            UploadPhotoBody body = new UploadPhotoBody(files.getBytes(), files.getContentType());
            return rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
                message.getMessageProperties().setHeader("command", "uploadPhotoCommand");
                return message;
            }, new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            return new UploadPhotoResponse("Invalid Input", false);
        }
    }
    @GetMapping("/downloadPhoto/{filename}")
    public ResponseEntity downloadPhoto(@PathVariable String filename) {
        try {
            DownloadPhotoBody body = new DownloadPhotoBody(filename);
            DownloadPhotoResponse response = rabbitTemplate.convertSendAndReceiveAsType(QUEUE, body, message -> {
                message.getMessageProperties().setHeader("command", "downloadPhotoCommand");
                return message;
            }, new ParameterizedTypeReference<>() {
            });
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(response.getContentType()))
                    .body(response.getPhotoByteArray());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new DownloadPhotoResponse(e.getMessage(), false, null, null));
        }
    }
}
