package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;
import study.sns.domain.entity.UserGroup;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByUserAndGroup(User user, Group group);
}
