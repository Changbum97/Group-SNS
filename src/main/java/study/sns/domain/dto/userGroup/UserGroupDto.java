package study.sns.domain.dto.userGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.dto.user.UserRole;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto {

    private String nickname;
    private UserRole userRole;
    private Integer groupStoryCount;    // 해당 유저가 그룹에 작성한 스토리 수
    private String joinedAt;            // 유저가 그룹에 참여한 날짜

    public static List<UserGroupDto> ofList(List<UserGroup> userGroups) {
        List<UserGroupDto> userGroupDtos = new ArrayList<>();

        for (UserGroup userGroup : userGroups) {
            userGroupDtos.add(of(userGroup));
        }

        return userGroupDtos;
    }

    public static UserGroupDto of(UserGroup userGroup) {
        return UserGroupDto.builder()
                .nickname(userGroup.getUser().getNickname())
                .userRole(userGroup.getUser().getUserRole())
                .groupStoryCount(0)
                .build();
    }
}
