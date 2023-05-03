package study.sns.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.sns.domain.Response;
import study.sns.domain.dto.user.UserDto;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.domain.dto.user.UserLoginRequest;
import study.sns.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<?> join(UserJoinRequest req) {
        UserDto dto = userService.saveUser(req);
        return Response.success(dto);
    }

    @PostMapping("/login")
    public Response<?> login(UserLoginRequest req) {
        String jwtToken = userService.login(req);
        return Response.success(jwtToken);
    }

    @GetMapping("/check-loginId")
    public Response<?> checkLoginId(@RequestParam String loginId) {
        Boolean pass = userService.checkLoginId(loginId);
        return Response.success(pass);
    }

    @GetMapping("/check-nickname")
    public Response<?> checkNickname(@RequestParam String nickname) {
        Boolean pass = userService.checkNickname(nickname);
        return Response.success(pass);
    }

}
