package com.ScalableTeam.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadPhotoBody {
    byte[] file;//MultipartFile files;
    String contentType;
}
