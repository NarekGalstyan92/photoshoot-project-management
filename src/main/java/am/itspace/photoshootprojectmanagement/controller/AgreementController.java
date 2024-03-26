package am.itspace.photoshootprojectmanagement.controller;

import am.itspace.photoshootprojectmanagement.component.PaginationAttributesComponent;
import am.itspace.photoshootprojectmanagement.entity.Agreement;
import am.itspace.photoshootprojectmanagement.security.SpringUser;
import am.itspace.photoshootprojectmanagement.service.AgreementService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/agreements")
public class AgreementController {

    private final AgreementService agreementService;

    private final PaginationAttributesComponent paginationAttributesComponent;

    @GetMapping
    public String agreementsPage(ModelMap modelMap,
                                 @AuthenticationPrincipal SpringUser springUser,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "size", required = false, defaultValue = "3") int size,
                                 @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy,
                                 @RequestParam(value = "order", required = false, defaultValue = "DESC") String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), orderBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Agreement> agreementsPage = agreementService.findAll(pageable, springUser);
        modelMap.addAttribute("agreements", agreementsPage);

        paginationAttributesComponent.addPaginationAttributes(
                modelMap, page, size, orderBy, order, agreementsPage.getTotalPages());

        return "test/agreements";
    }

    @GetMapping("/create")
    public String createAgreementPage(ModelMap modelMap,
                                      @RequestParam("bookingId") int bookingId) {
        modelMap.addAttribute("bookingId", bookingId);
        return "test/createAgreement";
    }

    @PostMapping("/create")
    public String createAgreement(@RequestParam("bookingId") int bookingId,
                                  @ModelAttribute Agreement agreement,
                                  @AuthenticationPrincipal SpringUser springUser) {
        agreementService.save(bookingId, agreement, springUser);
        return "redirect:/agreements";
    }

    @GetMapping("/update/{id}")
    public String updatePage(ModelMap modelMap,
                             @PathVariable("id") int id,
                             @AuthenticationPrincipal SpringUser springUser) {
        modelMap.addAttribute("agreement", agreementService.findById(id, springUser).get());
        return "test/updateAgreement";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Agreement agreement,
                         @AuthenticationPrincipal SpringUser springUser) {
        agreementService.update(agreement, springUser);
        return "redirect:/agreements";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id,
                             @AuthenticationPrincipal SpringUser springUser) {
        agreementService.deleteById(id, springUser);
        return "redirect:/agreements";
    }
}
