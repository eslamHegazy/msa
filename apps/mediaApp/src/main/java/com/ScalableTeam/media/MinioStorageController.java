package com.ScalableTeam.media;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InvalidResponseException;
import io.minio.messages.Bucket;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class MinioStorageController {
    @Autowired
    MinioClient minioClient;
    @Value("${minio.bucket.name}")
    String defaultBucketName;
    @Value("${minio.url}")
    String minioUrl;

    @GetMapping(path = "/buckets")
    public List<String> listBuckets() {
        try {
            return minioClient.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getImageExtensions(String contentType){
        if (contentType.equals(MediaType.IMAGE_PNG_VALUE))
            return "png";
        if (contentType.equals(MediaType.IMAGE_JPEG_VALUE))
            return "jpeg";
        if (contentType.equals(MediaType.IMAGE_GIF_VALUE))
            return "gif";
        throw new RuntimeException("Unsupported file");
    }

    @PostMapping(path = "/upload"
    )
    public Map<String, String> putFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        try{
            String contentType = files.getContentType();
            String extension = getImageExtensions(contentType);
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
            Map<String, String> result = new HashMap<>();
            result.put("image_url", minioUrl+"/"+defaultBucketName+"/"+fileNewName);
            return result;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam(value = "file") String file) throws IOException {
        try {
            String contentType = URLConnection.guessContentTypeFromName(file);
            System.out.println("content type from file name is: "+contentType);
            InputStream obj = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(file)
                            .build()
            );
            byte[] data = IOUtils.toByteArray(obj);
            obj.close();
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", contentType)
//                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                    .body(resource);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @DeleteMapping(path = "/remove")
    public ResponseEntity<HashMap> removeFile(@RequestParam(value = "fileUrl") String fileUrl) throws IOException {
        try{
            URL url = new URL(fileUrl);
            String objectPath = url.getPath().substring(1);
            String bucketName = objectPath.substring(0, objectPath.indexOf("/"));
            String objectName = objectPath.substring(1+ objectPath.indexOf("/"));
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            HashMap<String, String> result = new HashMap();
            result.put("success", "yes");
            return ResponseEntity
                    .ok()
                    .body(result);
        }
        catch (ErrorResponseException| InvalidResponseException e){
            HashMap<String, String> result = new HashMap();
            result.put("success", "no");
            result.put("message", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(result);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
