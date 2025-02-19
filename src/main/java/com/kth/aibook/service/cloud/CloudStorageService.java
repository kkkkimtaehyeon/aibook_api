package com.kth.aibook.service.cloud;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    String uploadFile(MultipartFile file);
    String uploadFiles(List<MultipartFile> files);
    String getFile(String fileName);
    void removeFile(String fileName);
}
