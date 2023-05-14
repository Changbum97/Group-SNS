package study.sns.domain.dto.story;

import com.amazonaws.services.s3.AmazonS3;
import lombok.*;
import study.sns.domain.entity.Story;
import study.sns.domain.entity.UploadImage;
import study.sns.domain.enum_class.StoryScope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoryDto implements Comparable<StoryDto> {

    private Long id;
    private String title;
    private String body;
    private StoryScope scope;
    private LocalDate date;
    private List<String> uploadImagesUrl;

    private Long userId;
    private String userNickname;
    private Long groupId;
    private String groupName;

    public static StoryDto of(Story story, AmazonS3 amazonS3, String bucket) {
        return StoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .body(story.getBody())
                .scope(story.getScope())
                .date(story.getDate())
                .userId(story.getUserGroup().getUser().getId())
                .userNickname(story.getUserGroup().getUser().getNickname())
                .groupId(story.getUserGroup().getGroup().getId())
                .groupName(story.getUserGroup().getGroup().getName())
                .uploadImagesUrl( makeUploadImagesUrl(story.getUploadImages(), amazonS3, bucket) )
                .build();
    }

    private static List<String> makeUploadImagesUrl(List<UploadImage> uploadImages, AmazonS3 amazonS3, String bucket) {
        if (uploadImages == null) return null;

        List<String> result = new ArrayList<>();
        for (UploadImage uploadImage : uploadImages) {
            result.add( amazonS3.getUrl(bucket, uploadImage.getSavedFilename()).toString() );
        }
        return result;
    }

    @Override
    public int compareTo(StoryDto o) {
        if (o.date.isBefore(date)) {
            return 1;
        } else if (o.date.isAfter(date)) {
            return -1;
        } else {
            return (int) (id - o.id);
        }
    }
}
