package study.sns.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.UserGroup;
import study.sns.domain.enum_class.GroupRole;

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
    private Integer totalStoryCount;
    private String createdAt;
    private GroupRole groupRole;

    public static GroupDto of(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .enterCode(group.getEnterCode())
                .memberCount(group.getUserGroups().size())
                .totalStoryCount(getTotalStoryCount(group))
                .createdAt(group.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .groupRole(group.getGroupRole())
                .build();
    }

    private static Integer getTotalStoryCount(Group group) {
        int result = 0;
        for (UserGroup userGroup : group.getUserGroups()) {
            if (userGroup.getStories() != null) {
                result += userGroup.getStories().size();
            }
        }
        return result;
    }
}
