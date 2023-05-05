package study.sns.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import study.sns.domain.Response;
import study.sns.domain.dto.user.*;
import study.sns.service.UserService;

import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("/logout")
    public Response<String> logout(Authentication auth) {
        userService.logout(auth.getName());
        return Response.success("SUCCESS");
    }

    @PostMapping("/nickname")
    public Response<String> setNickname(@ModelAttribute UserSetNicknameRequest req) {
        return Response.success(userService.setNickname(req.getNickname(), req.getAccessToken()));
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

    @GetMapping("/check-email")
    public Response<Boolean> checkEmail(@RequestParam String email) {
        Boolean pass = userService.checkEmail(email);
        return Response.success(pass);
    }
}
