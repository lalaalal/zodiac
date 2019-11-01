package org.zodiac.delivery.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zodiac.delivery.DeliveryDB;
import org.zodiac.delivery.SHA256Util;
import org.zodiac.delivery.model.Delivery;
import org.zodiac.delivery.model.Status;
import org.zodiac.delivery.model.User;

@Controller
public class DeliveryController {

    private void setDeliveryModelByNo(int no, Model model) {
        try {
            DeliveryDB deliveryDB = new DeliveryDB();
            Delivery delivery = deliveryDB.findDeliveryByNo(no);

            if (delivery.getMethod() == Delivery.POSTPAY)
                model.addAttribute("method", "착불");
            else
                model.addAttribute("method", "선불");
            model.addAttribute("describe", delivery.getDescribe());
            model.addAttribute("sender", delivery.getSenderName());
            model.addAttribute("senderPhone", delivery.getSenderPhone());
            model.addAttribute("recipient", delivery.getRecipientName());
            model.addAttribute("recipientPhone", delivery.getRecipientPhone());
            model.addAttribute("recipientAddress", delivery.getRecipientAddress());
            if (delivery.getStatus() == Delivery.CHECKING)
                model.addAttribute("status", "확인중");
            else if (delivery.getStatus() == Delivery.DONE)
                model.addAttribute("status", "배송 완료");
            else if (delivery.getStatus() == Delivery.YES)
                model.addAttribute("status", "배송 준비");
            else
                model.addAttribute("status", "취소됨");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "index";
    }

    @GetMapping("/search")
    public String search(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        String no = request.getParameter("no");
        setDeliveryModelByNo(Integer.parseInt(no), model);

        return "search";
    }

    @GetMapping("/check/delivery")
    public String check(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        String no = request.getParameter("no");
        String senderId = request.getParameter("senderid");
        String hash = request.getParameter("hash");

        if (!hash.equals(SHA256Util.SHA256(no + senderId))) {
            model.addAttribute("status", false);
            model.addAttribute("text", "변조된 URL 입니다");

            return "status";
        }

        setDeliveryModelByNo(Integer.parseInt(no), model);

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
                String hash = SHA256Util.SHA256(String.valueOf(id) + sender.getId());
                String message = String.format(ReservationController.MESSAGE, id, sender.getId(), hash);
                ReservationController.sendMessage(phone, message);
            }        
            else
                model.addAttribute("status", Status.STATUS_FAILED);

            
        } catch(Exception e) {
            System.out.println("An exception occured while connecting sql server");
            model.addAttribute("status", Status.STATUS_FAILED);
        }
        
        return "status";
    } 

    @PostMapping("/update/status")
    public String updateStatus(Model model, HttpServletRequest request) {
        String check = request.getParameter("check");
        try {
            DeliveryDB deliveryDB = new DeliveryDB();
            if (check.equals("yes"))
                deliveryDB.updateStatus(Delivery.YES);
            else
                deliveryDB.updateStatus(Delivery.CANCELED);
        } catch(Exception e) {
            e.printStackTrace();
        }
        

        return "status";
    }

    @RequestMapping("/delivery/guide/cost")
    public String costGuide(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "cost";
    }

    @RequestMapping("/delivery/guide/usage")
    public String usgaeGuide(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "usage";
    }

    @RequestMapping("/intro")
    public String intro(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);

        return "intro";
    }
}