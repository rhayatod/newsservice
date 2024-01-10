package com.investasikita.news.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to News Service.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    MinioConfiguration minio;

    public static class MinioConfiguration {
        String endPoint;
        String bucket_news;
        String accessKeyId;
        String accessKeySecret;

        /**
         * @return the endPoint
         */
        public String getEndPoint() {
            return endPoint;
        }

        /**
         * @param endPoint the endPoint to set
         */
        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        /**
         * @return the bucket
         */

        public String getBucket_news() {
            return bucket_news;
        }

        public void setBucket_news(String bucket_news) {
            this.bucket_news = bucket_news;
        }

        /**
         * @return the accessKeyId
         */
        public String getAccessKeyId() {
            return accessKeyId;
        }

        /**
         * @param accessKeyId the accessKeyId to set
         */
        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        /**
         * @return the accessKeySecret
         */
        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        /**
         * @param accessKeySecret the accessKeySecret to set
         */
        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

    }

    /**
     * @return the minio
     */
    public MinioConfiguration getMinio() {
        return minio;
    }

    /**
     * @param minio the minio to set
     */
    public void setMinio(MinioConfiguration minio) {
        this.minio = minio;
    }
}
