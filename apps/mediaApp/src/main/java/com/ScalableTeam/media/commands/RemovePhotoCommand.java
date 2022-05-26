package com.ScalableTeam.media.commands;


import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import models.media.RemovePhotoBody;
import models.media.RemovePhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

public class RemovePhotoCommand implements ICommand<RemovePhotoBody, RemovePhotoResponse> {
    @Autowired
    MinioClient minioClient;
    @Override
    public RemovePhotoResponse execute(RemovePhotoBody body) {
        try {
            String fileUrl = body.getFileUrl();
            URL url = new URL(fileUrl);
            String objectPath = url.getPath().substring(1);
            String bucketName = objectPath.substring(0, objectPath.indexOf("/"));
            String objectName = objectPath.substring(1+ objectPath.indexOf("/"));
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            return new RemovePhotoResponse("Successful Deletion", true);
        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
            return new RemovePhotoResponse(e.getMessage(), true);
        }
    }
}
