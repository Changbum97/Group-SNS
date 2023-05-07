package study.sns.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shops")
public class ShopController {

    @GetMapping("")
    public String shopMainPage(Model model) {
        model.addAttribute("message", "상점은 준비중입니다!");
        model.addAttribute("nextUrl", "/");
        return "pages/printMessage";
    }
}
