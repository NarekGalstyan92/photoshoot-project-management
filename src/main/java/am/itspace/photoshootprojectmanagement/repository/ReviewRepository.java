package am.itspace.photoshootprojectmanagement.repository;

import am.itspace.photoshootprojectmanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
