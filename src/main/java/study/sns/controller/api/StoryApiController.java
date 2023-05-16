package study.sns.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import study.sns.domain.Response;
import study.sns.domain.dto.story.StoryAddRequest;
import study.sns.domain.dto.story.StoryDto;
import study.sns.domain.dto.story.StoryEditRequest;
import study.sns.domain.dto.story.StoryListRequest;
import study.sns.service.StoryService;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@Api(description = "스토리 기능")
public class StoryApiController {

    private final StoryService storyService;

    @PostMapping(value = "")
    @ApiOperation(value = "스토리 추가", notes = "scope(스토리 공개 범위) => private 또는 public 입력")
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
    public Response<StoryDto> storyDetail(@PathVariable Long storyId, @ApiIgnore Authentication auth) {
        return Response.success(storyService.getStory(auth.getName(), storyId));
    }

    @DeleteMapping("/{storyId}")
    @ApiOperation(value = "스토리 삭제")
    public Response<String> storyDelete(@PathVariable Long storyId, @ApiIgnore Authentication auth) {
        storyService.deleteStory(auth.getName(), storyId);
        return Response.success("삭제 되었습니다.");
    }

    @PutMapping(value = "/{storyId}")
    @ApiOperation(value = "스토리 수정", notes = "scope(스토리 공개 범위) => private 또는 public 입력<br/>이미지를 업로드하지 않으면 기존의 이미지 유지, 업로드하면 기존의 이미지 삭제")
    public Response<StoryDto> editStory(@ApiIgnore Authentication auth,
                                       @PathVariable Long storyId,
                                       @RequestPart(value = "storyEditRequest") StoryEditRequest req,
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return Response.success(storyService.editStory(auth.getName(), storyId, req, images));
    }
}
