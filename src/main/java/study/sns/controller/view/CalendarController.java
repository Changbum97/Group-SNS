package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.sns.domain.dto.group.GroupDto;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.service.GroupService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final GroupService groupService;

    @GetMapping("")
    public String calendarMainPage(@RequestParam(required = false) Integer year,
                                   @RequestParam(required = false) Integer month,
                                   @RequestParam(required = false) String groupName,
                                   @RequestParam(required = false, defaultValue = "calendar") String type,
                                   Model model, Authentication auth) {
        if (year == null || month == null) {
            LocalDateTime now = LocalDateTime.now();
            model.addAttribute("year", now.getYear());
            model.addAttribute("month", now.getMonthValue());
        } else {
            model.addAttribute("year", year);
            model.addAttribute("month", month);
        }

        List<GroupDto> groupList = groupService.getGroupList(auth.getName());
        if (groupList.isEmpty()) {
            model.addAttribute("message", "최소 하나의 그룹에는 속해있어야 합니다!");
            model.addAttribute("nextUrl", "/groups");
            return "pages/printMessage";
        }

        model.addAttribute("groupList", groupList);

        if (groupName == null) {
            model.addAttribute("groupName", groupList.get(0).getName());
        } else {
            model.addAttribute("groupName", groupName);
        }

        model.addAttribute("type", type);

        return "pages/calendars/main";
    }
}
