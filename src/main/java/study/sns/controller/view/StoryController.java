package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.sns.domain.dto.group.GroupDto;
import study.sns.domain.dto.group.GroupRequest;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.domain.dto.story.StoryDto;
import study.sns.domain.dto.story.StoryEditRequest;
import study.sns.domain.entity.Group;
import study.sns.domain.entity.Story;
import study.sns.service.GroupService;
import study.sns.service.StoryService;

import java.util.List;

@Controller
@RequestMapping("/stories")
@RequiredArgsConstructor
public class StoryController {

    private final GroupService groupService;
    private final StoryService storyService;

    @GetMapping("/add")
    public String storyAddPage(Model model, Authentication auth,
                               @RequestParam(required = false) String group) {
        List<GroupDto> groupDtos = groupService.getGroupList(auth.getName());
        if (groupDtos.size() == 0) {
            model.addAttribute("message", "최소 하나의 그룹에는 속해있어야 합니다!");
            model.addAttribute("nextUrl", "/groups");
            return "pages/printMessage";
        }

        model.addAttribute("storyAddRequest", new StoryAddRequest(group));
        model.addAttribute("groups", groupDtos);

        return "pages/stories/add";
    }

    @GetMapping("/{storyId}")
    public String storyDetailPage(@PathVariable Long storyId, Model model, Authentication auth) {
        model.addAttribute("story", storyService.getStory(auth.getName(), storyId));
        return "pages/stories/detail";
    }

    @GetMapping("/{storyId}/edit")
    public String storyEditPage(@PathVariable Long storyId, Model model, Authentication auth) {
        StoryDto storyDto = storyService.getStory(auth.getName(), storyId);
        Group group = groupService.findByName(storyDto.getGroupName());

        model.addAttribute("groupName", group.getName());
        model.addAttribute("maxImages", group.getGroupRole().getMaxImage());
        model.addAttribute("storyId", storyId);

        model.addAttribute("storyEditRequest",
                new StoryEditRequest(storyDto.getTitle(), storyDto.getBody(),
                                        storyDto.getScope().name(), storyDto.getDate()));
        return "pages/stories/edit";
    }
}
