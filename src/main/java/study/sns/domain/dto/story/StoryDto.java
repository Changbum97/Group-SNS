package study.sns.domain.dto.story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.sns.domain.entity.UploadImage;
import study.sns.domain.enum_class.StoryScope;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryDto {

    private Long id;
    private String title;
    private String body;
    private StoryScope scope;
    private List<UploadImage> uploadImages;

    private Long userId;
    private Long groupId;
}
