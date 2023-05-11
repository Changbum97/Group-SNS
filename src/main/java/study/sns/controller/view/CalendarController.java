package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/calendars")
@RequiredArgsConstructor
public class CalendarController {

    @GetMapping("")
    public String calendarMainPage(@RequestParam(required = false) Integer year,
                                   @RequestParam(required = false) Integer month,
                                   Model model) {
        if (year == null || month == null) {
            LocalDateTime now = LocalDateTime.now();
            model.addAttribute("year", now.getYear());
            model.addAttribute("month", now.getMonthValue());
        } else {
            model.addAttribute("year", year);
            model.addAttribute("month", month);
        }
        return "pages/calendars/main";
    }
}