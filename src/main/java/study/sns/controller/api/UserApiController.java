package study.sns.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.sns.domain.Response;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<?> join(@RequestBody UserJoinRequest req) {
        return Response.success(userService.saveUser(req));
    }

}
