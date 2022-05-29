package com.ScalableTeam.media;

import com.ScalableTeam.media.commands.DownloadPhotoCommand;
import com.ScalableTeam.media.commands.RemovePhotoCommand;
import com.ScalableTeam.media.commands.UploadPhotoCommand;
import com.ScalableTeam.models.media.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.net.URLConnection;

@SpringBootTest
public class MediaAppTests {
    @Autowired
    private ApplicationContext context;

    public UploadPhotoResponse upload(String filePath) throws Exception{
        UploadPhotoCommand uploadPhotoCommand = context.getBean(UploadPhotoCommand.class);
        Resource resource = new ClassPathResource(filePath);
        InputStream f = resource.getInputStream();
        byte[] b = IOUtils.toByteArray(f);
        String originalFileName = "any_dummy_name.jpg";
        String contentType = URLConnection.guessContentTypeFromName(originalFileName);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(originalFileName, originalFileName, contentType, b);
        UploadPhotoBody body = new UploadPhotoBody(mockMultipartFile);
        UploadPhotoResponse r = uploadPhotoCommand.execute(body);
        return r;
    }
    public DownloadPhotoResponse download(String objectName) throws Exception{
        DownloadPhotoCommand downloadPhotoCommand = context.getBean(DownloadPhotoCommand.class);
        DownloadPhotoResponse r = downloadPhotoCommand.execute(new DownloadPhotoBody(objectName));
        return r;
    }
    public RemovePhotoResponse remove(String url) throws Exception{
        RemovePhotoCommand removePhotoCommand = context.getBean(RemovePhotoCommand.class);
        RemovePhotoResponse r = removePhotoCommand.execute(new RemovePhotoBody(url));
        return r;
    }
    @Test
    public void Upload_Then_Download_Test_1() throws Exception{
        UploadPhotoResponse uploadResponse = upload("test_image.jpg");
        String uploaded_url = uploadResponse.getMessage();
        Assert.isTrue(uploadResponse.isSuccessful(), "The upload should've been successful");
        DownloadPhotoResponse downloadResponse = download(uploaded_url.substring(uploaded_url.lastIndexOf("/")+1));
        Assert.isTrue(downloadResponse.isSuccessful(), "The download should'be been successful");
        Assert.notNull(downloadResponse.getResource(), "The file should've been returned, not null!");
    }
    @Test
    public void download_nonExistingFile_Test() throws Exception{
        DownloadPhotoResponse downloadResponse = download("nonexistinggg.jpg");
        Assert.isTrue(!downloadResponse.isSuccessful(), "The download should've failed");
        Assert.isNull(downloadResponse.getResource(), "Null should've been returned!");
    }
    @Test
    public void delete_existing() throws Exception{
        UploadPhotoResponse uploadResponse = upload("test_image.jpg");
        String uploaded_url = uploadResponse.getMessage();
        Assert.isTrue(uploadResponse.isSuccessful(), "The upload should've been successful");
        RemovePhotoResponse removeResponsee = remove(uploaded_url);
        Assert.isTrue(removeResponsee.isSuccessful(), "The deletion should've been successful");
    }



}
