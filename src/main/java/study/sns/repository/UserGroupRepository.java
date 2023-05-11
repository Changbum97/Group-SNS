package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

}
