package com.kth.aibook.service.cloud;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
    String uploadMultiPartFile(MultipartFile file);
    String uploadBytes(byte[] bytes, String format);
    void removeFile(String fileName);
    String getPreSignedUrlForUpdate(String key);
}
