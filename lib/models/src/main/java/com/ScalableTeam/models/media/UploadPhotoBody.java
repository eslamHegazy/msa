package com.ScalableTeam.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadPhotoBody {
   byte[] file;//MultipartFile files;
   String contentType;
}
