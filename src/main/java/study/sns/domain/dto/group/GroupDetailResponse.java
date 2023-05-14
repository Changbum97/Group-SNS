package study.sns.domain.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.dto.userGroup.UserGroupDto;
import study.sns.domain.entity.Group;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailResponse {

    private GroupDto groupDto;
    private List<UserGroupDto> userGroupDtos;

    public static GroupDetailResponse of(Group group) {
        return GroupDetailResponse.builder()
                .groupDto(GroupDto.of(group))
                .userGroupDtos(UserGroupDto.ofList(group.getUserGroups()))
                .build();
    }

}
