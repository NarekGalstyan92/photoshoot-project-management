package am.itspace.photoshootprojectmanagement.service;

import am.itspace.photoshootprojectmanagement.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    Review save(Review review);

    Page<Review> findAll(Pageable pageable);

    Optional<Review> findById(int id);

    Review update(Review review);

    void deleteById(int id);

}
