package com.ScalableTeam.media.commands;

import org.springframework.http.MediaType;

public class Utils {
    public static String getImageExtensions(String contentType){
        if (contentType.equals(MediaType.IMAGE_PNG_VALUE))
            return "png";
        if (contentType.equals(MediaType.IMAGE_JPEG_VALUE))
            return "jpeg";
        if (contentType.equals(MediaType.IMAGE_GIF_VALUE))
            return "gif";
        throw new RuntimeException("Unsupported file");
    }
}
