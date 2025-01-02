package com.wait.app.core.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 天
 *
 * description: minio配置
 */

@Data
@Configuration
public class MinioConfig {

    @Value(value = "${minio.client.endpoint}")
    private String endpoint;

    @Value(value = "${minio.client.accessKey}")
    private String accessKey;

    @Value(value = "${minio.client.secretKey}")
    private String secretKey;

    @Value(value = "${minio.client.bucketName}")
    private String bucketName;

    @Value(value = "${minio.client.pathPrefix}")
    private String pathPrefix;

    @Value(value = "${minio.client.virtualUrl}")
    private String virtualUrl;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey,secretKey)
                .build();
    }
}
