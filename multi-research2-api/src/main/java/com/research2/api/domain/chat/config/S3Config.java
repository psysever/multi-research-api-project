package com.research2.api.domain.chat.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.access-key}")
    private String accessKey;
    @Value("${spring.cloud.aws.secret-key}")
    private String secretKey;
    @Value("${spring.cloud.aws.end-point}")
    private String endpoint;
    @Value("${spring.cloud.aws.region")
    private String region;


    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

    }
}

