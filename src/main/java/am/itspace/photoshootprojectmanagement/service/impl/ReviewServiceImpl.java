package am.itspace.photoshootprojectmanagement.service.impl;

import am.itspace.photoshootprojectmanagement.entity.Review;
import am.itspace.photoshootprojectmanagement.repository.ReviewRepository;
import am.itspace.photoshootprojectmanagement.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Optional<Review> findById(int id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            throw new EntityNotFoundException("Review does not exists!");
        }

        return reviewOptional;
    }

    @Override
    public Review update(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(int id) {
        findById(id);

        reviewRepository.deleteById(id);
    }

    public void addPaginationAttributes(ModelMap modelMap, int page, int size, String orderBy, String order, int totalPages) {
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("orderBy", orderBy);
        modelMap.addAttribute("order", order);

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
