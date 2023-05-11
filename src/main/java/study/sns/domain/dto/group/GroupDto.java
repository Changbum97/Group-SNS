package study.sns.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.entity.Group;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private Long id;
    private String name;
    private String enterCode;
    private Integer memberCount;
    private Integer storyCount;
    private String createdAt;

    public static GroupDto of(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .enterCode(group.getEnterCode())
                .memberCount(group.getUserGroups().size())
                .storyCount(0)
                .createdAt(group.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
