package study.sns.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import study.sns.jwt.JwtTokenUtil;

@Controller
public class HomeController {

    @GetMapping(value = {"", "/", "/home"})
    public String home(@CookieValue(name = "accessToken", required = false) String accessToken,
                       Model model) {
        if (accessToken != null) {
            String loginId = JwtTokenUtil.getLoginId(accessToken, "root4568#");
            model.addAttribute("loginId", loginId);
        }
        return "pages/home";
    }
}
