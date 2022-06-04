package com.ScalableTeam.media;

import com.ScalableTeam.media.commands.DownloadPhotoCommand;
import com.ScalableTeam.media.commands.RemovePhotoCommand;
import com.ScalableTeam.media.commands.UploadPhotoCommand;
import com.ScalableTeam.models.media.*;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@SpringBootTest
public class MediaAppTests {
    @Autowired
    MinioClient minioClient;
    @Autowired
    private ApplicationContext context;

    public UploadPhotoResponse upload(String filePath) throws Exception {
        UploadPhotoCommand uploadPhotoCommand = context.getBean(UploadPhotoCommand.class);
        Resource resource = new ClassPathResource(filePath);
        InputStream f = resource.getInputStream();
        byte[] b = IOUtils.toByteArray(f);
        String originalFileName = "any_dummy_name.jpg";
        String contentType = URLConnection.guessContentTypeFromName(originalFileName);
        UploadPhotoBody body = new UploadPhotoBody(b, contentType);
        UploadPhotoResponse r = uploadPhotoCommand.execute(body);
        return r;
    }

    public DownloadPhotoResponse download(String objectName) throws Exception {
        DownloadPhotoCommand downloadPhotoCommand = context.getBean(DownloadPhotoCommand.class);
        DownloadPhotoResponse r = downloadPhotoCommand.execute(new DownloadPhotoBody(objectName));
        return r;
    }

    public void remove(String url) throws Exception {
        RemovePhotoCommand removePhotoCommand = context.getBean(RemovePhotoCommand.class);
        removePhotoCommand.execute(new RemovePhotoBody(url));
    }

    @Test
    public void Upload_Then_Download_Test_1() throws Exception {
        UploadPhotoResponse uploadResponse = upload("test_image.jpg");
        String uploaded_url = uploadResponse.getMessage();
        Assert.isTrue(uploadResponse.isSuccessful(), "The upload should've been successful");
        DownloadPhotoResponse downloadResponse = download(uploaded_url.substring(uploaded_url.lastIndexOf("/") + 1));
        Assert.isTrue(downloadResponse.isSuccessful(), "The download should'be been successful");
        Assert.notNull(downloadResponse.getPhotoByteArray(), "The file should've been returned, not null!");
    }

    @Test
    public void download_nonExistingFile_Test() throws Exception {
        DownloadPhotoResponse downloadResponse = download("nonexistinggg.jpg");
        Assert.isTrue(!downloadResponse.isSuccessful(), "The download should've failed");
        Assert.isNull(downloadResponse.getPhotoByteArray(), "Null should've been returned!");
    }

    @Test
    public void delete_existing() throws Exception {
        UploadPhotoResponse uploadResponse = upload("test_image.jpg");
        String uploaded_url = uploadResponse.getMessage();
        Assert.isTrue(uploadResponse.isSuccessful(), "The upload should've been successful");
        remove(uploaded_url);
        URL url = new URL(uploaded_url);
        String objectPath = url.getPath().substring(1);
        String bucketName = objectPath.substring(0, objectPath.indexOf("/"));
        String objectName = objectPath.substring(1 + objectPath.indexOf("/"));
        ErrorResponseException e = Assertions.assertThrows(ErrorResponseException.class, () -> {
            InputStream obj = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        });
        Assertions.assertEquals(404, e.response().code(), "The file should have been deleted!");
    }
}
