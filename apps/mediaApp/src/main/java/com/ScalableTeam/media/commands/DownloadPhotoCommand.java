package com.ScalableTeam.media.commands;

import com.ScalableTeam.models.media.DownloadPhotoBody;
import com.ScalableTeam.models.media.DownloadPhotoResponse;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URLConnection;

@Service
public class DownloadPhotoCommand implements ICommand<DownloadPhotoBody, DownloadPhotoResponse> {

    @Autowired
    MinioClient minioClient;
    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Override
    public DownloadPhotoResponse execute(DownloadPhotoBody body) {
        try {
            System.out.println("Default Bucket name: " + defaultBucketName);
            String fileName = body.getFileName();
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            System.out.println("content type from file name is: " + contentType);
            InputStream obj = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(fileName)
                            .build()
            );
            byte[] data = IOUtils.toByteArray(obj);
            obj.close();
//            ByteArrayResource resource = new ByteArrayResource(data);
//            return new DownloadPhotoResponse("Success", true, contentType, resource);
            return new DownloadPhotoResponse("Success", true, contentType, data);
        } catch (Exception e) {
            return new DownloadPhotoResponse(e.getMessage(), false, "", null);
        }
    }
}
