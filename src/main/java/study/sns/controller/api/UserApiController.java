package study.sns.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.sns.domain.Response;
import study.sns.domain.dto.user.UserDto;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.domain.dto.user.UserLoginRequest;
import study.sns.domain.dto.user.UserLoginResponse;
import study.sns.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserDto> join(UserJoinRequest req) {
        return Response.success(userService.saveUser(req));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(UserLoginRequest req) {
        return Response.success(userService.login(req));
    }

    @GetMapping("/check-loginId")
    public Response<Boolean> checkLoginId(@RequestParam String loginId) {
        Boolean pass = userService.checkLoginId(loginId);
        return Response.success(pass);
    }

    @GetMapping("/check-nickname")
    public Response<Boolean> checkNickname(@RequestParam String nickname) {
        Boolean pass = userService.checkNickname(nickname);
        return Response.success(pass);
    }

}
