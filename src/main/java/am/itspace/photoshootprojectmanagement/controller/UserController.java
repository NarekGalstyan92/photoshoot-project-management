package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.User;
import am.itspace.photoshootprojectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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

        return "";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("picture") MultipartFile multipartFile) {
        userService.save(user, multipartFile);

        return "";
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
