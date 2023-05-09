package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.sns.domain.dto.user.UserJoinRequest;
import study.sns.domain.dto.user.UserLoginRequest;
import study.sns.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "pages/users/join";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "pages/users/login";
    }

    @GetMapping("/set-nickname")
    public String setNicknamePage(@CookieValue(name = "accessToken") String accessToken, Model model) {
        String nickname = userService.findByAccessToken(accessToken).getNickname();

        if (!nickname.equals("")) {
            model.addAttribute("message", "닉네임이 설정되어 있습니다!");
            model.addAttribute("nextUrl", "/");
            return "pages/printMessage";
        }

        return "pages/users/nickname";
    }

    @GetMapping("/find-id-pw")
    public String findIdPwPage() {
        return "pages/users/find-id-pw";
    }
}
