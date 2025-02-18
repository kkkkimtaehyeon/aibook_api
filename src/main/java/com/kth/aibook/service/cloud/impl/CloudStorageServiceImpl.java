package com.kth.aibook.service.cloud.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.kth.aibook.service.cloud.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CloudStorageServiceImpl implements CloudStorageService {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String fileName = getUniqueFileName(multipartFile);
        putObject(multipartFile, fileName);
        return fetchUrl(fileName);
    }

    @Override
    public String getFile(String fileName) {
        return "";
    }

    @Override
    public void removeFile(String fileName) {

    }

    private String getUniqueFileName(MultipartFile file) {
        String ogFileName = file.getOriginalFilename().split("\\.")[0];
        String fileExtension = file.getOriginalFilename().split("\\.")[1];
        return ogFileName + UUID.randomUUID() + "." + fileExtension;
    }

    private void putObject(MultipartFile multipartFile, String fileName) {
        try {
            File file = convert(multipartFile, fileName);
            amazonS3.putObject(BUCKET, fileName, file);
        } catch (IOException e) {
            //TODO: 예외처리 추가 필요
            throw new RuntimeException("파일 변환 중 오류가 발생했습니다!");
        }
    }

    private String fetchUrl(String fileName) {
        return amazonS3.getUrl(BUCKET, fileName).toString();
    }

    private File convert(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
