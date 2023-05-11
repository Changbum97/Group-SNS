package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.sns.domain.dto.group.GroupAddRequest;
import study.sns.domain.entity.User;
import study.sns.service.GroupService;
import study.sns.service.UserService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("")
    public String groupMainPage(@CookieValue(name = "accessToken") String accessToken, Model model) {
        User user = userService.findByAccessToken(accessToken);
        model.addAttribute("user", user);
        model.addAttribute("groupList", groupService.getGroupList(user.getLoginId()));
        return "pages/groups/list";
    }

    @GetMapping("/add")
    public String groupNewPage(@CookieValue(name = "accessToken") String accessToken, Model model) {
        model.addAttribute("user", userService.findByAccessToken(accessToken));
        model.addAttribute("groupAddRequest", new GroupAddRequest());
        return "pages/groups/add";
    }
}
