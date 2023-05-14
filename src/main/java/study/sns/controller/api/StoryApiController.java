package study.sns.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import study.sns.domain.Response;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.domain.dto.story.StoryDto;
import study.sns.domain.dto.story.StoryListRequest;
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

    @GetMapping("/list")
    @ApiOperation(value = "스토리 리스트 조회", notes = "그룹명, 연도, 월에 해당하는 스토리 리스트 조회")
    public Response<List<StoryDto>> getStoryList(@ApiIgnore Authentication auth, StoryListRequest req) {
        return Response.success(storyService.getStoryList(auth.getName(), req));
    }

    @GetMapping("/{storyId}")
    @ApiOperation(value = "스토리 상세 조회")
    public Response<StoryDto> storyDetailPage(@PathVariable Long storyId, @ApiIgnore Authentication auth) {
        return Response.success(storyService.getStory(auth.getName(), storyId));
    }
}
