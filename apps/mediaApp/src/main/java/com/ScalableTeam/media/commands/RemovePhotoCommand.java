package com.ScalableTeam.media.commands;


import com.ScalableTeam.models.media.RemovePhotoBody;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class RemovePhotoCommand implements ICommand<RemovePhotoBody, Void> {
    @Autowired
    MinioClient minioClient;

    @Override
    public Void execute(RemovePhotoBody body) {
        try {
            String fileUrl = body.getFileUrl();
            URL url = new URL(fileUrl);
            String objectPath = url.getPath().substring(1);
            String bucketName = objectPath.substring(0, objectPath.indexOf("/"));
            String objectName = objectPath.substring(1 + objectPath.indexOf("/"));
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
