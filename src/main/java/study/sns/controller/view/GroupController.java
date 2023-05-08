package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.sns.service.UserService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final UserService userService;

    @GetMapping("")
    public String groupMainPage(@CookieValue(name = "accessToken") String accessToken, Model model) {
        model.addAttribute("user", userService.findByAccessToken(accessToken));
        return "pages/groups/main";
    }
}
