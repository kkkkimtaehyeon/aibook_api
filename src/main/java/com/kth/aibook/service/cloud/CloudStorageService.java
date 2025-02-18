package com.kth.aibook.service.cloud;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
    String uploadFile(MultipartFile file);
    String getFile(String fileName);
    void removeFile(String fileName);
}
