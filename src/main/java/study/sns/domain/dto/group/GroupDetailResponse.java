package study.sns.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.dto.user.UserDto;
import study.sns.domain.dto.userGroup.UserGroupDto;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailResponse {

    private Long id;
    private String name;
    private String enterCode;
    private Integer storyCount;
    private String createdAt;
    private List<UserGroupDto> userGroupDtos;

    public static GroupDetailResponse of(Group group) {
        return GroupDetailResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .enterCode(group.getEnterCode())
                .storyCount(0)
                .createdAt(group.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .userGroupDtos(UserGroupDto.ofList(group.getUserGroups()))
                .build();
    }
}
