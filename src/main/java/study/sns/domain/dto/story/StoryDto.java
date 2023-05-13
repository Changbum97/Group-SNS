package study.sns.domain.dto.story;

import lombok.*;
import study.sns.domain.entity.Story;
import study.sns.domain.entity.UploadImage;
import study.sns.domain.enum_class.StoryScope;

import java.time.LocalDate;
import java.util.List;

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
    private List<UploadImage> uploadImages;

    private Long userId;
    private Long groupId;

    public static StoryDto of(Story story) {
        return StoryDto.builder()
                .id(story.getId())
                .title(story.getTitle())
                .body(story.getBody())
                .scope(story.getScope())
                .date(story.getDate())
                .userId(story.getUserGroup().getUser().getId())
                .groupId(story.getUserGroup().getGroup().getId())
                .build();
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
