package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.Booking;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.BookingService;
import am.itspace.photoshootprojectmanagement.service.EventCategoryService;
import am.itspace.photoshootprojectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    private final UserService userService;

    private final EventCategoryService eventCategoryService;

    @GetMapping
    public String bookingPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                              @RequestParam(value = "orderBy", required = false, defaultValue = "bookingDate") String orderBy,
                              @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.fromString(order), orderBy);
        Page<Booking> bookingsPage = bookingService.findAll(pageable);

        // There is a bug in users/bookingPage. User can see only his booking, but the booking
        // page size is actually greater, so pagination works out of my control.
        // The problem comes from the upper "bookingService.findAll(pageable)" method. ⬆️
        // it should return the bookings only related to the currentUser, so tha pagination can work correct.

        int totalPages = bookingsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        modelMap.addAttribute("bookings", bookingsPage);
        modelMap.addAttribute("currentUser", springUser);

        return "users/bookingPage";
    }

    @GetMapping("/create")
    public String createBookingPage(ModelMap modelMap) {
        modelMap.addAttribute("eventCategories", eventCategoryService.findAll());

        return "users/createBooking";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute Booking booking, @AuthenticationPrincipal SpringUser springUser) {
        User user = userService.findByEmail(springUser.getUsername())
                .orElseThrow(() -> new NoSuchElementException("No user found with email: " + springUser.getUsername()));
        booking.setUser(user);
        bookingService.save(booking);

        return "redirect:/bookings";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                             @RequestParam(value = "orderBy", required = false, defaultValue = "bookingDate") String orderBy,
                             @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        modelMap.addAttribute("booking", bookingService.findById(id).orElseThrow(() -> new NoSuchElementException("No booking found with id: " + id)));
        modelMap.addAttribute("eventCategories", eventCategoryService.findAll());

        return "users/updateBooking";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute Booking booking, @PathVariable("id") int id, @AuthenticationPrincipal SpringUser springUser) {

        Optional<User> userOptionalByEmail = userService.findByEmail(springUser.getUsername());

        booking.setUser(userOptionalByEmail.orElseThrow(() -> new NoSuchElementException("No user found")));
        booking.setId(id);
        bookingService.update(booking);

        return "redirect:/bookings";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {

        Optional<Booking> bookingOptional = bookingService.findById(id);
        if (bookingOptional.isPresent()) {
            bookingService.deleteById(id);

            return "redirect:/bookings";
        }

        return "redirect:/bookings";
    }
}
