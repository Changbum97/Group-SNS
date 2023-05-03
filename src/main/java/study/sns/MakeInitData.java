package study.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import study.sns.domain.dto.user.UserRole;
import study.sns.domain.entity.User;
import study.sns.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class MakeInitData {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void makeUser() {
        User admin = User.builder()
                .loginId("admin")
                .password(encoder.encode("1234"))
                .nickname("관리자")
                .userRole(UserRole.ADMIN)
                .build();

        userRepository.save(admin);

        User user1 = User.builder()
                .loginId("user")
                .password(encoder.encode("1234"))
                .nickname("지나가는행인")
                .userRole(UserRole.USER)
                .build();

        userRepository.save(user1);
    }
}
