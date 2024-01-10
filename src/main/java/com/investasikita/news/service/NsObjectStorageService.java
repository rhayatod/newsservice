package com.investasikita.news.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface NsObjectStorageService {

    public enum ClientObjectType { ID_FILE, IMAGE };

    public String store(ClientObjectType objectType, MultipartFile file, String userLogin);

    public InputStream load(ClientObjectType objectType, String objectName);

    public void copyFromTemp(String objectName);

    public void deleteFromTemp(String objectName);

    public InputStream loadForFrontEnd(ClientObjectType objectType, String objectName);

    public String storeFromDataIntegration(ClientObjectType objectType, MultipartFile file, String fileName);

    public byte[] toByteArray(ClientObjectType objectType, String imageId);
}

