package com.ScalableTeam.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DownloadPhotoResponse {
    private String message;
    private boolean successful;
    private String contentType; //TODO: map it to headers
    private byte[] photoByteArray;
//    private ByteArrayResource resource;
}
