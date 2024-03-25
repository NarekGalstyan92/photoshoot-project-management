package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.Booking;
import am.itspace.photoshootprojectmanagement.entity.Role;
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
    public String bookingsPage(ModelMap modelMap,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                               @RequestParam(value = "orderBy", required = false, defaultValue = "bookingDate") String orderBy,
                               @RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
                               @AuthenticationPrincipal SpringUser springUser) {

        // Create a Pageable object based on pagination parameters
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.fromString(order), orderBy);

        // Retrieve bookings based on user role
        Page<Booking> bookingsPage;
        if (springUser.getUser().getRole() == Role.ADMIN) {
            bookingsPage = bookingService.findAll(pageable); // Admins see all bookings
        } else {
            bookingsPage = bookingService.findByUserId(pageable, springUser.getUser().getId());  // Regular users see only their bookings
        }

        // Generate page numbers for pagination links if multiple pages exist
        int totalPages = bookingsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        modelMap.addAttribute("bookings", bookingsPage);

        return "users/bookingPage";
    }

    @GetMapping("/create")
    public String createBookingPage(ModelMap modelMap) {
        modelMap.addAttribute("eventCategories", eventCategoryService.findAll());

        return "users/createBooking";
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute Booking booking,
                                @AuthenticationPrincipal SpringUser springUser) {
        booking.setUser(userService.findByEmail(springUser.getUsername()).get());
        bookingService.save(booking);

        return "redirect:/bookings";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id) {

        modelMap.addAttribute("booking", bookingService.findById(id).get());
        modelMap.addAttribute("eventCategories", eventCategoryService.findAll());

        return "users/updateBooking";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Booking booking,
                         @AuthenticationPrincipal SpringUser springUser) {

        booking.setUser(springUser.getUser());
        bookingService.update(booking);

        return "redirect:/bookings";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {

        bookingService.deleteById(id);

        return "redirect:/bookings";
    }
}
