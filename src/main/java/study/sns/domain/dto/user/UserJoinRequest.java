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
public class UserJoinRequest {

    private String loginId;
    private String password;
    private String nickname;

    public User toEntity(String encodedPassword, UserRole userRole) {
        return User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .userRole(userRole)
                .build();
    }
}
