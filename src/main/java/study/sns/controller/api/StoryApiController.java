package study.sns.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import study.sns.domain.Response;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.domain.dto.story.StoryDto;
import study.sns.service.StoryService;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@Api(description = "스토리 기능")
public class StoryApiController {

    private final StoryService storyService;

    @PostMapping("")
    @ApiOperation(value = "스토리 추가")
    public Response<StoryDto> addStory(@ApiIgnore Authentication auth,
                                       @RequestPart(value = "storyAddRequest") StoryAddRequest req,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return Response.success(storyService.addStory(auth.getName(), req, images));
    }
}
