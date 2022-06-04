package com.ScalableTeam.media;

import com.ScalableTeam.media.commands.DownloadPhotoCommand;
import com.ScalableTeam.media.commands.RemovePhotoCommand;
import com.ScalableTeam.media.commands.UploadPhotoCommand;
import com.ScalableTeam.models.media.*;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;

@AllArgsConstructor
@Component
public class AppRunner implements CommandLineRunner {

    final DownloadPhotoCommand downloadPhotoCommand;
    final UploadPhotoCommand uploadPhotoCommand;
    final RemovePhotoCommand removePhotoCommand;

    @Override
    public void run(String... args) throws Exception {
//        successfulDownload("s5f914728-5879-4ce9-9f89-a6231052e162.jpeg");
//        successfulUpload("/test_image.jpg");
//        UDRD("test_image.jpg");
    }

    public void testDownload(String fileName) throws Exception {
        DownloadPhotoResponse r = downloadPhotoCommand.execute(new DownloadPhotoBody(fileName));
        System.out.println("Download message" + r.getMessage());
        if (r.isSuccessful()) {
            System.out.println("Download content type = " + r.getContentType());
//        System.out.println(r.getResource());
            File f = new File("run_output.jpg");
            FileOutputStream fos = new FileOutputStream(f);
            System.out.println("Downloaded at: " + f.getAbsolutePath());
//            ByteArrayResource b = r.getResource();
//            fos.write(b.getByteArray());
            fos.write(r.getPhotoByteArray());
        }
    }

    public String testUpload(String fileName) throws Exception {
        Resource resource = new ClassPathResource(fileName);
        InputStream f = resource.getInputStream();
        byte[] b = IOUtils.toByteArray(f);
        String originalFileName = "xabc.jpg";
        String contentType = URLConnection.guessContentTypeFromName(originalFileName);
        UploadPhotoBody body = new UploadPhotoBody(b, contentType);
        UploadPhotoResponse r = uploadPhotoCommand.execute(body);
        System.out.println("Upload isSuccessful = " + r.isSuccessful());
        System.out.println("Upload Message / url = " + r.getMessage());
        return r.getMessage();
    }

    public void testDelete(String url) {
        RemovePhotoBody body = new RemovePhotoBody(url);
//        RemovePhotoResponse r = removePhotoCommand.execute(body);
        removePhotoCommand.execute(body);
//        System.out.print("Delete isSuccessful = "+r.isSuccessful());
//        System.out.print("Delete Message = "+r.getMessage());
    }

    public void UDRD(String fileName) throws Exception {
        String uploadedUrl = testUpload(fileName);
        String uploadedFileName = uploadedUrl.substring(uploadedUrl.lastIndexOf('/') + 1);
        testDownload(uploadedFileName);
        Thread.sleep(10000);
        testDelete(uploadedUrl);
        Thread.sleep(15000);
        testDownload(uploadedFileName);
    }
}
