package study.sns.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import study.sns.domain.Response;
import study.sns.domain.dto.group.GroupDetailResponse;
import study.sns.domain.dto.group.GroupRequest;
import study.sns.domain.dto.group.GroupDto;
import study.sns.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Api(description = "그룹 기능")
public class GroupApiController {

    private final GroupService groupService;

    @PostMapping("")
    @ApiOperation(value = "그룹 추가", notes = "그룹을 추가 성공 시, 그룹 참여까지 진행")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "그룹 이름, 2~15자 제한, 공백 불가"),
            @ApiImplicitParam(name = "enterCode", value = "입장 코드, 5~20자 제한, 한글, 공백 불가")})
    public Response<GroupDto> addGroup(GroupRequest req, @ApiIgnore Authentication auth) {
        return Response.success(groupService.addGroup(req, auth.getName()));
    }

    @GetMapping("/join")
    @ApiOperation(value = "그룹 참여")
    public Response<GroupDto> joinGroup(GroupRequest req, @ApiIgnore Authentication auth) {
        return Response.success(groupService.joinGroup(req, auth.getName()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "내가 속한 그룹 리스트")
    public Response<List<GroupDto>> getMyGroupList(@ApiIgnore Authentication auth) {
        return Response.success(groupService.getGroupList(auth.getName()));
    }

    @GetMapping("/{groupId}")
    @ApiOperation(value = "그룹 상세 정보 조회")
    public Response<GroupDetailResponse> groupDetail(@PathVariable Long groupId, @ApiIgnore Authentication auth) {
        return Response.success(groupService.getGroupDetail(groupId, auth.getName()));
    }

    @DeleteMapping("/{groupId}")
    @ApiOperation(value = "그룹 삭제")
    public Response<String> groupDelete(@PathVariable Long groupId, @ApiIgnore Authentication auth) {
        groupService.deleteGroup(groupId, auth.getName());
        return Response.success("그룹이 삭제되었습니다.");
    }

    @PutMapping("/{groupId}")
    @ApiOperation(value = "그룹 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "그룹 이름, 2~15자 제한, 공백 불가"),
            @ApiImplicitParam(name = "enterCode", value = "입장 코드, 5~20자 제한, 한글, 공백 불가")})
    public Response<GroupDto> editGroup(@PathVariable Long groupId, GroupRequest req, @ApiIgnore Authentication auth) {
        return Response.success(groupService.editGroup(groupId, req, auth.getName()));
    }

    @DeleteMapping("/leave/{groupId}")
    @ApiOperation(value = "그룹 탈퇴")
    public Response<String> groupUserDelete(@PathVariable Long groupId, @ApiIgnore Authentication auth) {
        groupService.deleteGroupUser(groupId, auth.getName());
        return Response.success("탈퇴 되었습니다.");
    }

    @GetMapping("/check-name")
    @ApiOperation(value = "그룹명 중복 체크 통과", notes = "true: 중복 X, false: 중복 O")
    public Response<Boolean> checkName(@RequestParam String name) {
        Boolean pass = groupService.checkName(name);
        return Response.success(pass);
    }

}
