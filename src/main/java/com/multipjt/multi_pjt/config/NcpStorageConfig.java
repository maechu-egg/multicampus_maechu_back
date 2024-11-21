package com.multipjt.multi_pjt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class NcpStorageConfig {
    @Value("${ncp.objectstorage.access-key}")
    private String accessKey;

    @Value("${ncp.objectstorage.secret-key}")
    private String secretKey;

    @Value("${ncp.objectstorage.endpoint}")
    private String endpoint;

    @Value("${ncp.objectstorage.bucket-name}")
    private String bucketName;

    @Value("${ncp.objectstorage.region}")
    private String region;

    @Bean
    public AmazonS3Client objectStorageClient() {
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public String getBucketName() {
        return bucketName;
    }
}
