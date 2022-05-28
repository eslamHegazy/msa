package com.ScalableTeam.media.commands;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import com.ScalableTeam.models.media.UploadPhotoBody;
import com.ScalableTeam.models.media.UploadPhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class UploadPhotoCommand implements ICommand<UploadPhotoBody, UploadPhotoResponse>{

    @Autowired
    MinioClient minioClient;
    @Value("${minio.bucket.name}")
    String defaultBucketName;
    @Value("${minio.url}")
    String minioUrl;

    @Override
    public UploadPhotoResponse execute(UploadPhotoBody body) {
        try{
            MultipartFile files = body.getFiles();
            String contentType = files.getContentType();
            String extension = Utils.getImageExtensions(contentType);
            String fileNewName = UUID.randomUUID().toString()+'.'+extension;
            System.out.println("extension of file received from user is: "+extension);
            InputStream inputStream = files.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(fileNewName)
                            .stream(inputStream, -1,10485760)
                            .contentType(contentType)
                            .build()
            );
            String path = defaultBucketName+"/"+fileNewName;
            String directUrl = minioUrl+"/"+path;
            return new UploadPhotoResponse(directUrl, true);
        }
        catch (Exception e){
//            throw new RuntimeException(e.getMessage());
            return new UploadPhotoResponse(e.getMessage(), false);
        }
    }
}
