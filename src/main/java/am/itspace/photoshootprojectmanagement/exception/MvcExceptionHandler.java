package am.itspace.photoshootprojectmanagement.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView defaultErrorHandler(RuntimeException ex) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("exceptionMsg", ex.getMessage());
        mav.setViewName("pages-404");

        return mav;
    }
}
