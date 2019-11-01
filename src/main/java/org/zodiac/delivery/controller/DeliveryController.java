package org.zodiac.delivery.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zodiac.delivery.DeliveryDB;
import org.zodiac.delivery.model.Delivery;
import org.zodiac.delivery.model.Status;
import org.zodiac.delivery.model.User;

@Controller
public class DeliveryController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "index";
    }

    @RequestMapping("/check")
    public String check(Model model,HttpServletRequest request ) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "check";
    }

    @PostMapping("/add/delivery")
    public String addDelivery(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        String describe = request.getParameter("describe");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String domain = request.getParameter("domain");
        String address = request.getParameter("address");
        String _method = request.getParameter("method");
        int method = 0;
        if (_method.equals("pre"))
            method = Delivery.PREPAY;
        else method = Delivery.POSTPAY;
            
        User sender = (User) session.getAttribute("user");

        try {
            DeliveryDB deliveryDB = new DeliveryDB();
            int id = deliveryDB.addDelivery(describe, sender, name, phone, email + domain, address, method);

            if (id != -1) {
                model.addAttribute("status", Status.STATUS_SUCCEED);
                ReservationController.snedMessage(phone, String.format(ReservationController.MESSAGE, describe, sender.getName(), sender.getPhone()));
            }        
            else
                model.addAttribute("status", Status.STATUS_FAILED);

            
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("An exception occured while connecting sql server");
            model.addAttribute("status", Status.STATUS_FAILED);
        }
        
        return "status";
    }
}