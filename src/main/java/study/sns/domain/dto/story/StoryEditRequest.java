package study.sns.domain.dto.story;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryEditRequest {

    private String title;
    private String body;
    private String scope;
    private LocalDate date;
}
