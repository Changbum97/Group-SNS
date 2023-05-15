package study.sns.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.Story;
import study.sns.domain.entity.UploadImage;
import study.sns.domain.entity.UserGroup;
import study.sns.repository.UploadImageRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final UploadImageRepository uploadImageRepository;
    private final UserGroupService userGroupService;

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

    public void deleteAllByGroup(Group group) {
        List<UserGroup> userGroups = userGroupService.findByGroup(group);
        if (!userGroups.isEmpty()) {
            for (UserGroup userGroup : userGroups) {
                if (userGroup.getStories() != null) {
                    for (Story story : userGroup.getStories()) {
                        if (story.getUploadImages() != null) {
                            for (UploadImage uploadImage : story.getUploadImages()) {
                                System.out.println(uploadImage.getOriginalFilename());
                                amazonS3.deleteObject(bucket, uploadImage.getSavedFilename());
                            }
                        }
                    }
                }
            }
        }
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
