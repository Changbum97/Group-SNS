package study.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.sns.domain.entity.UploadImage;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
