package study.sns.domain.dto.story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryListRequest {

    private String groupName;
    private Integer year;
    private Integer month;
}