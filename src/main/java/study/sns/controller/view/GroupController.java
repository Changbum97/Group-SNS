package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import study.sns.domain.dto.group.GroupRequest;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.service.GroupService;
import study.sns.service.UserGroupService;
import study.sns.service.UserService;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final UserGroupService userGroupService;

    @GetMapping("")
    public String groupMainPage(Authentication auth, Model model) {
        model.addAttribute("groupList", groupService.getGroupList(auth.getName()));
        return "pages/groups/list";
    }

    @GetMapping("/add")
    public String groupNewPage(Model model) {
        model.addAttribute("groupRequest", new GroupRequest());
        return "pages/groups/add";
    }

    @GetMapping("/join")
    public String groupJoinPage(Model model) {
        model.addAttribute("groupRequest", new GroupRequest());
        return "pages/groups/join";
    }

    @GetMapping("/{groupId}")
    public String groupDetailPage(@PathVariable Long groupId, Authentication auth, Model model) {
        model.addAttribute("groupDetail", groupService.getGroupDetail(groupId, auth.getName()));
        return "pages/groups/detail";
    }

    @GetMapping("/{groupId}/edit")
    public String groupEditPage(@PathVariable Long groupId, Authentication auth, Model model) {
        User user = userService.findByLoginId(auth.getName());
        Group group = groupService.findById(groupId);
        userGroupService.findByUserAndGroup(user, group);

        model.addAttribute("groupId", groupId);
        model.addAttribute("groupRequest", new GroupRequest(group.getName(), group.getEnterCode()));
        return "pages/groups/edit";
    }
}
