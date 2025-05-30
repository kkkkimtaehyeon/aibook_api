package com.kth.aibook.service.cloud.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kth.aibook.service.cloud.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CloudStorageServiceImpl implements CloudStorageService {
    private final AmazonS3 s3;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET;

    public String getPreSignedUrlForUpdate(String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET, key)
                .withMethod(HttpMethod.PUT)
//                .withContentType("audio/wav")
                .withExpiration(getPreSignedExpiration());
        return s3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String fileName = getUniqueFileName(multipartFile);
        putMultiPartFile(multipartFile, fileName);
        return fetchUrl(fileName);
    }

    @Override
    public String uploadBytes(byte[] bytes, String format) {
        String fileName = UUID.randomUUID().toString(); // 임의로 파일 이름 저장
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(format);
        s3.putObject(BUCKET, fileName, byteArrayInputStream, metadata);
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

    private void putMultiPartFile(MultipartFile multipartFile, String fileName) {
        try {
            File file = convert(multipartFile, fileName);
            s3.putObject(BUCKET, fileName, file);
        } catch (IOException e) {
            //TODO: 예외처리 추가 필요
            throw new RuntimeException("파일 변환 중 오류가 발생했습니다!");
        }
    }

    private String fetchUrl(String fileName) {
        return s3.getUrl(BUCKET, fileName).toString();
    }

    private File convert(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    private Date getPreSignedExpiration() {
        long expirationMillis = System.currentTimeMillis() + (1000 * 60 * 15); // 15분
        return new Date(expirationMillis);
    }
}
