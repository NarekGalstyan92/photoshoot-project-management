package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.Role;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String usersPage(ModelMap modelMap,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "2") int size,
                            @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy,
                            @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), orderBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> usersPage = userService.findByIsDeleted(false, pageable);

        modelMap.addAttribute("users", usersPage);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("orderBy", orderBy);
        modelMap.addAttribute("order", order);

        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        return "";
    }

    @GetMapping("/register")
    public String registerPage(ModelMap modelMap,
                               @RequestParam(value = "msg", required = false) String msg) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }

        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("picture") MultipartFile multipartFile) {
        return userService.registerUser(user, multipartFile).get();
    }

    @GetMapping("/loginPage")
    public String loginPage(@AuthenticationPrincipal SpringUser springUser) {

        if (springUser == null) {
            return "loginPage";
        }

        return "redirect:/";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal SpringUser springUser) {
        return (springUser.getUser().getRole() == Role.ADMIN)
                ? "redirect:/users/admin/home"
                : "redirect:/";
    }

    @GetMapping("/admin/home")
    public String adminPage() {
        return "admin/home";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id) {
        modelMap.addAttribute("user", userService.findById(id).get());
        return "";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User user,
                         @RequestParam("picture") MultipartFile multipartFile) {
        userService.update(user, multipartFile);
        return "";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "";
    }

    @GetMapping("/deletePicture/{id}")
    public String deletePicture(@PathVariable("id") int id) {
        userService.deletePicture(id);
        return "";
    }
}
