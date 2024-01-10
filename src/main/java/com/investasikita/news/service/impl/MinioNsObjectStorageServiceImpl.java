package com.investasikita.news.service.impl;

import com.investasikita.news.config.ApplicationProperties;
import com.investasikita.news.security.AuthoritiesConstants;
import com.investasikita.news.security.SecurityUtils;
import com.investasikita.news.service.NsObjectStorageService;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.StringTokenizer;

@Service
public class MinioNsObjectStorageServiceImpl implements NsObjectStorageService {

    static final String MINIO_END_POINT = "https://play.minio.io:9000";
    //  static final String MINIO_END_POINT = "http://192.168.2.200:9001";
    static final String BUCKET_NEWS = "newsarticle111222333";
    private final Logger log = LoggerFactory.getLogger(MinioNsObjectStorageServiceImpl.class);
    private static final String ENTITY_NAME = "MinioNewsObjectStorageServiceImpl";

    private MinioClient minioClient = null;

    @Autowired
    ApplicationProperties appConfig;

    private boolean init() {
        try {
            this.minioClient = new MinioClient(appConfig.getMinio().getEndPoint(),
                appConfig.getMinio().getAccessKeyId(), appConfig.getMinio().getAccessKeySecret());
            return makeBucket(appConfig.getMinio().getBucket_news());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUserAuthorizedToLoadByName(String objectName) {
        if (objectName == null || objectName == "")
            return false;

        StringTokenizer sTokenizer = new StringTokenizer(objectName, "_");
        String userLogin = "";
        if (sTokenizer.hasMoreTokens())
            sTokenizer.nextToken(); // pass prefix
        if (sTokenizer.hasMoreTokens())
            sTokenizer.nextToken(); // pass timestamp
        if (sTokenizer.hasMoreTokens())
            userLogin = sTokenizer.nextToken(); // get UserLogin

        return ((SecurityUtils.isAuthenticated()
            && SecurityUtils.getCurrentUserLogin().get().toLowerCase().equals(userLogin.toLowerCase()))
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.CS)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.CSM)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.OPS)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.OPSM)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.FIN)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.FINM)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MK)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MKM)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.COMP)
            || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.BOD)
        ); // we only authorize users to load
        // their own file
    }

    private boolean makeBucket(String bucketName) {
        try {
            // Create bucket if it doesn't exist.
            boolean found = minioClient.bucketExists(bucketName);
            if (!found) {
                minioClient.makeBucket(bucketName);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String store(ClientObjectType objectType, MultipartFile file, String userLogin) {
        try {
            init();
            String bucketName = "";
            objectType = ClientObjectType.IMAGE;
            bucketName = MinioNsObjectStorageServiceImpl.BUCKET_NEWS;
            String objectName = constructObjectName(objectType, userLogin);
            minioClient.putObject(bucketName, objectName, file.getInputStream(), file.getContentType());
            return Base64.getEncoder().encodeToString(objectName.getBytes()); // file name is stored as encoded in DB
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String constructObjectName(ClientObjectType objectType, String userLogin) {
        String fileName = (userLogin != null) ? userLogin.toLowerCase() : "";
        String prefix = "IMAGE";
        if (SecurityUtils.isAuthenticated() && !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            fileName = SecurityUtils.getCurrentUserLogin().get().toLowerCase();
        }

        return prefix + Instant.now().toString() + "_" + fileName;
    }

    @Override
    public InputStream load(ClientObjectType objectType, String objectName) {
        objectName = new String(Base64.getDecoder().decode(objectName));
        log.debug("Enter in minio service impl, load object with id: " + objectName);
        if (isUserAuthorizedToLoadByName(objectName)) {
            try {
                boolean response = init();
                if (response) {
                    minioClient.statObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                    return minioClient.getObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void copyFromTemp(String objectName) {
        // get real object name
        objectName = new String(Base64.getDecoder().decode(objectName));
        try {
            if (isUserAuthorizedToLoadByName(objectName)) {
                boolean response = init();
                if (response) {
                    minioClient.statObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFromTemp(String objectName) {
        objectName = new String(Base64.getDecoder().decode(objectName));
        if (isUserAuthorizedToLoadByName(objectName)) {
            try {
                boolean response = init();
                if (response && SecurityUtils.isAuthenticated() && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
                    minioClient.removeObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public InputStream loadForFrontEnd(ClientObjectType objectType, String objectName) {
        objectName = new String(Base64.getDecoder().decode(objectName));
        log.debug("Enter in minio service impl, loadForFrontEnd object with id: " + objectName);
        try {
            boolean response = init();
            try {
                if (response) {
                    minioClient.statObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                    return minioClient.getObject(MinioNsObjectStorageServiceImpl.BUCKET_NEWS, objectName);
                }
            } catch (Exception e) {
                // return minioClient.getObject(MinioMfProductObjectStorageServiceImpl.BUCKET_CLIENT_TEMP,
                //         objectName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String storeFromDataIntegration(ClientObjectType objectType, MultipartFile file, String fileName) {
        try {
            init();
            String bucketName;

            bucketName = MinioNsObjectStorageServiceImpl.BUCKET_NEWS;

            String objectName = constructObjectName(objectType, fileName);
            minioClient.putObject(bucketName, objectName, file.getInputStream(), file.getContentType());
            return Base64.getEncoder().encodeToString(objectName.getBytes()); // file name is stored as encoded in DB
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] toByteArray(ClientObjectType objectType, String imageId) {
        InputStream inputStream = null;
        try {
            inputStream = loadForFrontEnd(objectType, imageId);
            if (inputStream == null)
                return null;
            byte[] outByte = IOUtils.toByteArray(inputStream);
            inputStream.close();
            return outByte;
//
//            if (inputStream != null) {
//                return IOUtils.toByteArray(inputStream);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try { if (inputStream != null) inputStream.close(); } catch(IOException e) {}
        }
        return null;
    }
    
}
