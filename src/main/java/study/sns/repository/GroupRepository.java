package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Boolean existsByName(String name);
}
