package com.svasant.movies.writer.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
@EnableSqs
public class AWSConfig {

    @Value("${cloud.aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.defaultClient();
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(awsCredentialsProvider())
                .build();
    }
    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        //TODO: consider switching to WebIdentityTokenCredentialsProvider once we figure out if we have the right add-ons
        return new EnvironmentVariableCredentialsProvider();
    }
}
