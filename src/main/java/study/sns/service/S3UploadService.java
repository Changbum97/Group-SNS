package study.sns.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.sns.domain.entity.Story;
import study.sns.domain.entity.UploadImage;
import study.sns.repository.UploadImageRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final UploadImageRepository uploadImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public UploadImage saveImage(MultipartFile multipartFile, Story story) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        // 원본 파일명 -> 서버에 저장된 파일명 (중복 방지)
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        String savedFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, savedFilename, multipartFile.getInputStream(), metadata);

        return uploadImageRepository.save(UploadImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .story(story)
                .build());
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
