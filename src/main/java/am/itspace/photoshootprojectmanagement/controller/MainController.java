package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.component.FileComponent;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FileComponent fileComponent;


    @GetMapping("/")
    public String mainPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            modelMap.addAttribute("currentUser", springUser);
            return "index";
        }
        return "redirect:/users/loginPage";
    }

    @GetMapping(value = "/getPicture", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPicture(@RequestParam("picName") String picName) {
        return fileComponent.getPicture(picName);
    }
}
