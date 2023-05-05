package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
    Boolean existsByEmail(String email);
}
