package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
