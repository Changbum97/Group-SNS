package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.sns.domain.dto.group.GroupDto;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.service.GroupService;

import java.util.List;

@Controller
@RequestMapping("/stories")
@RequiredArgsConstructor
public class StoryController {

    private final GroupService groupService;

    @GetMapping("/add")
    public String storyAddPage(Model model, Authentication auth) {
        List<GroupDto> groupDtos = groupService.getGroupList(auth.getName());
        if (groupDtos.size() == 0) {
            model.addAttribute("message", "최소 하나의 그룹에는 속해있어야 합니다!");
            model.addAttribute("nextUrl", "/groups");
            return "pages/printMessage";
        }

        model.addAttribute("storyAddRequest", new StoryAddRequest());
        model.addAttribute("groups", groupDtos);
        return "pages/stories/add";
    }
}
