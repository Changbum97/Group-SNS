package study.sns.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
    public String home(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("nickname", userService.findByLoginId(auth.getName()).getNickname());
        }
        return "pages/home";
    }
}
