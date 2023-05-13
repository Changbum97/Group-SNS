package study.sns.domain.dto.story;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryAddRequest {

    private String groupName;
    private String title;
    private String body;
    private String scope;
    private LocalDate date;
}
