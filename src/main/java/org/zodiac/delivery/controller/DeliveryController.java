package org.zodiac.delivery.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zodiac.delivery.model.User;

@Controller
public class DeliveryController {

    private void snedMessage(String phone, String message) {

    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/check")
    public String check() {
        return "check";
    }

    @PostMapping("/add/delivery")
    public String addDelivery(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.getAttribute("user");

        model.addAttribute("status", true);
        return "status";
    }
}