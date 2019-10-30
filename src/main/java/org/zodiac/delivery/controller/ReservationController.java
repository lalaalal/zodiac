package org.zodiac.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {
    @GetMapping("/delivery/reservation")
    public String reservation() {
        return "reservation";
    }
}