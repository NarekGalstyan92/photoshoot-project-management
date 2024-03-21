package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.entity.EventCategory;
import am.itspace.photoshootprojectmanagement.exception.InvalidEventCategoryException;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.EventCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/eventCategory")
@Slf4j
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    @GetMapping
    public String eventCategoryPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {

        modelMap.addAttribute("eventCategory", eventCategoryService.findAll());
        modelMap.addAttribute("springUser", springUser);

        return "users/events";
    }

    @GetMapping("/create")
    public String createEventCategoryPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        modelMap.addAttribute("currentUser", springUser);
        return "admin/createEvent";
    }

    @PostMapping("/create")
    public String eventCategory(@ModelAttribute EventCategory eventCategory) {
        log.info("eventCategory/create POST method called");
        eventCategoryService.save(eventCategory);
        log.info("CategoryService saved the event");

        return "redirect:/eventCategory";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id) {
        modelMap.addAttribute("eventCategory", eventCategoryService.findById(id).orElseThrow(() -> new NoSuchElementException("No Event found with id: " + id)));

        return "admin/updateEvent";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute EventCategory eventCategory, @PathVariable("id") int id) {

        Optional<EventCategory> eventCategoryOptional = eventCategoryService.findById(id);

        if (eventCategoryOptional.isEmpty()) {
            throw new InvalidEventCategoryException("Event not found with ID: " + id);
        }

        eventCategory.setId(id);
        eventCategoryService.update(eventCategory);

        return "redirect:/eventCategory";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id) {

        Optional<EventCategory> eventCategoryOptionalById = eventCategoryService.findById(id);

        if (eventCategoryOptionalById.isPresent()) {
            eventCategoryService.deleteById(id);

            return "redirect:/eventCategory";
        }

        return "redirect:/eventCategory";
    }
}
