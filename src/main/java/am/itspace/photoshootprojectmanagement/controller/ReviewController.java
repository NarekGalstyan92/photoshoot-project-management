package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.Review;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.service.ReviewService;
import am.itspace.photoshootprojectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final UserService userService;

    @GetMapping
    public String reviewsPage(ModelMap modelMap,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                              @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy,
                              @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), orderBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Review> reviewsPage = reviewService.findAll(pageable);

        modelMap.addAttribute("reviews", reviewsPage);

        int totalPages = reviewsPage.getTotalPages();
        reviewService.addPaginationAttributes(modelMap, page, size, orderBy, order, totalPages);

        return "";
    }

    @GetMapping("/create")
    public String createReviewPage() {
        return "";
    }

    @PostMapping("/create")
    public String review(@ModelAttribute Review review) {
        reviewService.save(review);

        return "";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                             @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy,
                             @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        modelMap.addAttribute("review", reviewService.findById(id).get());

        Sort sort = Sort.by(Sort.Direction.fromString(order), orderBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> usersPage = userService.findByIsDeleted(false, pageable);
        modelMap.addAttribute("users", usersPage);

        int totalPages = usersPage.getTotalPages();
        reviewService.addPaginationAttributes(modelMap, page, size, orderBy, order, totalPages);

        return "";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Review review) {
        reviewService.update(review);

        return "";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {
        reviewService.deleteById(id);

        return "";
    }
}
