package com.multipjt.multi_pjt.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client amazonS3Client;
    private final NcpStorageConfig ncpStorageConfig; // NCP 스토리지 설정 주입

    public String putFileToBucket(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is empty or null.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file has no original filename.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + originalFilename.replace(" ", "_");
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            // NCP 오브젝트 스토리지에 이미지 업로드 시 퍼블릭 읽기 권한 추가
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ncpStorageConfig.getBucketName(),
                    fileName,
                    inputStream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead); // 퍼블릭 읽기 권한 설정

            amazonS3Client.putObject(putObjectRequest);

            return fileName; // 업로드된 파일 이름 반환
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed: " + e.getMessage());
        }
    }

    public void deleteFileFromBucket(String fileName) {
        try {
            // NCP 오브젝트 스토리지에서 이미지 삭제
            amazonS3Client.deleteObject(ncpStorageConfig.getBucketName(), fileName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image deletion failed: " + e.getMessage());
        }
    }
}
