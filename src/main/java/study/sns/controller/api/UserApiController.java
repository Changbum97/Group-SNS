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
    public Response<?> join(@RequestBody UserJoinRequest req) {
        UserDto dto = userService.saveUser(req);
        return Response.success(dto);
    }

    @PostMapping("/login")
    public Response<?> login(@RequestBody UserLoginRequest req) {
        String jwtToken = userService.login(req);
        return Response.success(jwtToken);
    }

}
