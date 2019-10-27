package org.zodiac.delivery;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DeliveryController {
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "lalaalal");

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/check")
    public String check(Model model) {
        return "check";
    }
}