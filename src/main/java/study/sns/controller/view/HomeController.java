package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import study.sns.util.JwtTokenUtil;
import study.sns.service.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${jwt.token.secret}")
    private String secretKey;

    private final UserService userService;

    @GetMapping(value = {"", "/", "/home"})
    public String home(@CookieValue(name = "accessToken", required = false) String accessToken,
                       Model model) {
        if (accessToken != null) {
            model.addAttribute("nickname", userService.findByAccessToken(accessToken).getNickname());
        }
        return "pages/home";
    }
}
