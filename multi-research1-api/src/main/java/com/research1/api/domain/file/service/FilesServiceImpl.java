package com.research1.api.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.research1.api.domain.file.dto.req.FilesDeleteDto;
import com.research1.api.domain.file.dto.req.FilesUploadDto;
import com.research1.api.domain.file.entity.Files;
import com.research1.api.domain.file.repository.FilesRepository;
import com.research1.api.global.exception.CustomException;
import com.research1.api.global.exception.error.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class FilesServiceImpl implements FilesService {

    @Value("${spring.config.activate.on-profile}")
    private String activeProfile;


    private final AmazonS3Client publicS3Client;
    private final AmazonS3Client privateS3Client;


    @Value("${spring.cloud.aws.bucket-name-public}")
    private String bucketNamePublic;

    @Value("${spring.cloud.aws.bucket-name-private}")
    private String bucketNamePrivate;


    @Value("${spring.cloud.aws.cloudfront.public-url}")
    private String publicCloudFrontUrl;

    @Value("${spring.cloud.aws.cloudfront.private-url}")
    private String privateCloudFrontUrl;

    private final FilesRepository filesRepository;

    public FilesServiceImpl(
            @Qualifier("publicS3Client") AmazonS3Client publicS3Client,
            @Qualifier("privateS3Client") AmazonS3Client privateS3Client,
            FilesRepository filesRepository) {
        this.publicS3Client = publicS3Client;
        this.privateS3Client = privateS3Client;
        this.filesRepository = filesRepository;
    }


    @Override
    public Files getFile(int fileId) {
        return filesRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCodes.UserErrorCode.FILE_NOT_FOUND));
    }

    @Transactional
    @Override
    public int uploadFile(FilesUploadDto fileUploadDto) throws IOException {
        try {
            // Select a client based on file type
            boolean isPublic = "pub".equalsIgnoreCase(fileUploadDto.getFileType());
            String bucketName = isPublic ? bucketNamePublic : bucketNamePrivate;
            AmazonS3Client s3Client = isPublic ? publicS3Client : privateS3Client;

            MultipartFile file = fileUploadDto.getFile();
            String originalFilename = file.getOriginalFilename();


            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new CustomException(ErrorCodes.UserErrorCode.FILE_NOT_FOUND);
            }

            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }

            // Create a file name to be stored in S3
            String s3FileName = String.format("%s/%s/%s-%s",
                    activeProfile,
                    fileUploadDto.getFilePathType(),
                    UUID.randomUUID().toString().substring(0, 10),
                    originalFilename);


            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());


            if (isPublic) {
                metadata.setCacheControl("max-age=31536000");
            }

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    s3FileName,
                    inputStream,
                    metadata);


            putObjectRequest.setCannedAcl(
                    isPublic ? CannedAccessControlList.PublicRead : CannedAccessControlList.Private);


            s3Client.putObject(putObjectRequest);


            String fileUrl = generateFileUrl(isPublic, bucketName, s3FileName);


            Files entity = Files.builder()
                    .serverPath(fileUrl)
                    .fileType(fileUploadDto.getFileType())
                    .filePathType(fileUploadDto.getFilePathType())
                    .fileName(originalFilename)
                    .extension(extension)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            Files saved = filesRepository.save(entity);

            log.info("File upload successful: fileId={}, fileName={}, s3Key={}",
                    saved.getFileId(), originalFilename, s3FileName);

            return saved.getFileId();

        } catch (IOException e) {
            log.error("IO error occurred while uploading file: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while uploading the file: {}", e.getMessage(), e);
            throw new CustomException(ErrorCodes.UserErrorCode.FILE_UPLOAD_FAIL);
        }
    }


    private String generateFileUrl(boolean isPublic, String bucketName, String s3FileName) {
        if (isPublic && !publicCloudFrontUrl.isEmpty()) {

            return publicCloudFrontUrl + "/" + s3FileName;
        } else if (!isPublic && !privateCloudFrontUrl.isEmpty()) {

            return privateCloudFrontUrl + "/" + s3FileName;
        } else {

            AmazonS3Client s3Client = isPublic ? publicS3Client : privateS3Client;
            return s3Client.getUrl(bucketName, s3FileName).toString();
        }
    }


    @Transactional
    public void deleteFile(FilesDeleteDto deleteDto) {
        try {
            Files file = getFile(deleteDto.getFileId());

            boolean isPublic = "pub".equalsIgnoreCase(file.getFileType());
            String bucketName = isPublic ? bucketNamePublic : bucketNamePrivate;
            AmazonS3Client s3Client = isPublic ? publicS3Client : privateS3Client;


            String s3Key = extractS3KeyFromUrl(file.getServerPath());
            s3Client.deleteObject(bucketName, s3Key);


            filesRepository.deleteById(deleteDto.getFileId());

            log.info("File deletion completed: fileId={}, s3Key={}", deleteDto.getFileId(), s3Key);

        } catch (Exception e) {
            log.error("An error occurred while deleting file: {}", e.getMessage(), e);
            throw new CustomException(ErrorCodes.UserErrorCode.FILE_DELETE_FAIL);
        }
    }

    /**
     * URL에서 S3 키 추출
     */
    private String extractS3KeyFromUrl(String url) {
        // CloudFront URL이나 S3 URL에서 키 부분만 추출
        if (url.contains(publicCloudFrontUrl)) {
            return url.replace(publicCloudFrontUrl + "/", "");
        } else if (url.contains(privateCloudFrontUrl)) {
            return url.replace(privateCloudFrontUrl + "/", "");
        } else {
            // S3 직접 URL의 경우
            String[] parts = url.split("/");
            return String.join("/", java.util.Arrays.copyOfRange(parts, 3, parts.length));
        }
    }
}