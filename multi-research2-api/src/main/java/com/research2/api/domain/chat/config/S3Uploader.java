package com.research2.api.domain.chat.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3;

    @Value("${spring.cloud.aws.bucket-name}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFilename;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("S3 FAILED: " + e.getMessage(), e);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }
}