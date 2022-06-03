package com.ScalableTeam.media.commands;

import org.springframework.http.MediaType;

public class Utils {
    public static String getImageExtensions(String contentType) {
        if (MediaType.IMAGE_PNG_VALUE.equals(contentType))
            return "png";
        if (MediaType.IMAGE_JPEG_VALUE.equals(contentType))
            return "jpeg";
        if (MediaType.IMAGE_GIF_VALUE.equals(contentType))
            return "gif";
        throw new RuntimeException("You can upload only a JPG/PNG/GIF image!");
    }
}
