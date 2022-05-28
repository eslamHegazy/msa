package com.ScalableTeam.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadPhotoResponse {

    // In case of success, url of image.
    // In case of failure, the reason.
    private String message;
    private boolean successful;
}
