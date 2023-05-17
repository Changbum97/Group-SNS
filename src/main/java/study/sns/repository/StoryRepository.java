package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.Story;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
