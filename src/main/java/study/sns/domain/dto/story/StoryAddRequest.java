package study.sns.domain.dto.story;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoryAddRequest {

    private String group;
    private String title;
    private String body;
    private String scope;
    private List<MultipartFile> images;

}
