package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.Role;
import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Value("${project.picture.upload.directory}")
    private String uploadDirectory;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String usersPage(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.findByIsDeleted(false));
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
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            return "redirect:/users/register?msg=Email already in use";
        } else {

            // TODO create a method which sends a verification email to user

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user, multipartFile);
            return "redirect:/users/register?msg=User Registered";
        }
    }

    @GetMapping("/loginPage")
    public String loginPage(@AuthenticationPrincipal SpringUser springUser) {
        log.info("loginPage called");
        if (springUser == null) {
            return "loginPage";
        }
        log.info("SpringUser {} logged in", springUser.getUser().getEmail());
        return "redirect:/";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal SpringUser springUser) {

        User user = springUser.getUser();
        log.info("SpringUser {} is", springUser.getUser().getEmail());

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/home";
        }
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return "";
        }

        modelMap.addAttribute("user", userOptional.get());
        return "test/updateUser";
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
