package study.sns.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String loginId;
    private String nickname;
    private String email;
    private UserRole userRole;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }
}
