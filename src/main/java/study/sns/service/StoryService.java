package study.sns.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.domain.dto.story.StoryDto;
import study.sns.domain.dto.story.StoryListRequest;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.Story;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.enum_class.StoryScope;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.repository.StoryRepository;
import study.sns.repository.UserGroupRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserService userService;
    private final GroupService groupService;
    private final UserGroupRepository userGroupRepository;
    private final S3UploadService s3UploadService;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public StoryDto addStory(String loginId, StoryAddRequest req, List<MultipartFile> images) {
        User user = userService.findByLoginId(loginId);
        Group group = groupService.findByName(req.getGroupName());
        UserGroup userGroup = userGroupRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new AppException(ErrorCode.USER_GROUP_NOT_FOUND));

        StoryScope scope;
        if (req.getScope().toUpperCase().equals("PUBLIC")) scope = StoryScope.PUBLIC;
        else if (req.getScope().toUpperCase().equals("PRIVATE")) scope = StoryScope.PRIVATE;
        else throw new AppException(ErrorCode.BAD_REQUEST, "공개범위가 설정되지 않았습니다.");

        if (req.getDate() == null) throw new AppException(ErrorCode.INVALID_DATE, "날짜가 선택되지 않았습니다.");

        Story story = Story.builder()
                .userGroup(userGroup)
                .title(req.getTitle())
                .body(req.getBody())
                .scope(scope)
                .date(req.getDate())
                .build();

        Story savedStory = storyRepository.save(story);

        if (images != null) {
            try {
                for (MultipartFile image : images) {
                    s3UploadService.saveImage(image, savedStory);
                }
            } catch (IOException e) {
                throw new AppException(ErrorCode.S3_UPLOAD_FAIL);
            }
        }

        return StoryDto.of(savedStory, amazonS3, bucket);
    }

    public List<StoryDto> getStoryList(String loginId, StoryListRequest req) {
        User user = userService.findByLoginId(loginId);
        Group group = groupService.findByName(req.getGroupName());
        userGroupRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new AppException(ErrorCode.USER_GROUP_NOT_FOUND));

        List<StoryDto> result = new ArrayList<>();
        for (UserGroup userGroup : group.getUserGroups()) {
            for (Story story : userGroup.getStories()) {
                LocalDate firstDate = LocalDate.of(req.getYear(), req.getMonth(), 1);
                LocalDate lastDate = LocalDate.of(req.getYear(), req.getMonth(), firstDate.lengthOfMonth());
                if ((story.getDate().isAfter(firstDate) || story.getDate().equals(firstDate)) &&
                        (story.getDate().isBefore(lastDate) || story.getDate().equals(lastDate))) {
                    result.add(StoryDto.of(story, amazonS3, bucket));
                }
            }
        }

        Collections.sort(result);
        return result;
    }

    public StoryDto getStory(String loginId, Long storyId) {
        User loginUser = userService.findByLoginId(loginId);
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new AppException(ErrorCode.STORY_NOT_FOUND));

        // 로그인한 유저가 스토리에 접근할 수 있는지 확인
        Group storyGroup = story.getUserGroup().getGroup();
        if (!userGroupRepository.existsByUserAndGroup(loginUser, storyGroup)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION);
        }

        return StoryDto.of(story, amazonS3, bucket);
    }
}
