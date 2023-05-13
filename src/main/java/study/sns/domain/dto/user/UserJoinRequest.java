package study.sns.domain.dto.user;

import lombok.*;
import study.sns.domain.entity.User;
import study.sns.domain.enum_class.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {

    private String loginId;
    private String password;
    private String nickname;
    private String email;

    public User toEntity(String encodedPassword, UserRole userRole) {
        return User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .email(email)
                .userRole(userRole)
                .build();
    }
}
