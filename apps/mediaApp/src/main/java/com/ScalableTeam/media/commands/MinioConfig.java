package com.ScalableTeam.media.commands;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
    @Value("${minio.url}")
    String minioUrl;
    @Value("${minio.bucket.name}")
    String defaultMinioBucket;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            MinioClient client = MinioClient
                    .builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, accessSecret)
                    .build();
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(defaultMinioBucket).build()))
                client.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(defaultMinioBucket)
                                .build()
                );
            String policyJson = String.format(
                    "{\"Version\": \"2012-10-17\", \"Statement\": [{\"Sid\": \"PublicRead\", \"Effect\": \"Allow\", \"Principal\": {\"AWS\": [\"*\"]}, \"Action\": [\"s3:GetObject\",\"s3:GetObjectVersion\"    ],    \"Resource\": [\"arn:aws:s3:::%s/*\"    ]}    ]}",
                    defaultMinioBucket);
            client.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(defaultMinioBucket)
                            .config(policyJson)
                            .build()
            );
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
