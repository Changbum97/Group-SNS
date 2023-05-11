package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
    Boolean existsByName(String name);
}
