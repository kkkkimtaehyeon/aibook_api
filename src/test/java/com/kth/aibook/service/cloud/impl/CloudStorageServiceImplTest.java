//package com.kth.aibook.service.cloud.impl;
//
//import com.kth.aibook.service.cloud.CloudStorageService;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class CloudStorageServiceImplTest {
//    @Autowired
//    private CloudStorageService cloudStorageService;
//
//    @Test
//    void uploadFile() {
//    }
//
//    @Disabled
//    @DisplayName("S3 바이트 업로드 (.wav)")
//    @Test
//    void uploadBytes() throws IOException {
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource("short_audio_sample.wav").getFile()); // 파일 경로 가져오기
//        InputStream inputStream = new FileInputStream(file);
//        byte[] audioBytes = inputStream.readAllBytes();
//        String fileFormat = "audio/wav";
//
//        String url = cloudStorageService.uploadBytes(audioBytes, fileFormat);
//
//        assertNotNull(url);
//    }
//
//    @Test
//    void getFile() {
//    }
//
//    @Test
//    void removeFile() {
//    }
//}