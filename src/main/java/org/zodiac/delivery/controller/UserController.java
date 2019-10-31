package org.zodiac.delivery.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zodiac.delivery.DeliveryDB;
import org.zodiac.delivery.model.User;

@Controller
public class UserController {

    private static final Boolean STATUS_SUCCEED = true;
    private static final Boolean STATUS_FAILED = false;

    private String SHA256(String string) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(string.getBytes());
            byte byteData[] = sha.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0 ; i < byteData.length; i++){
                stringBuffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }

            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean checkUserExists(String id, String pw) {
        try {
            DeliveryDB db = new DeliveryDB();

            User user = db.findUserById(id);
            if (user == null)
                return false;
            if (user.getPw().equals(pw))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("SQL Error");
        }
        return false;
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String userId = request.getParameter("id");
        String userPw = request.getParameter("pw");
        userPw = SHA256(userPw);

        if (checkUserExists(userId, userPw)) {
            session.setAttribute("user", new User(userId, userPw));
            model.addAttribute("status", STATUS_SUCCEED);
        } else {
            model.addAttribute("status", STATUS_FAILED);
        }

        return "status";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.removeAttribute("user");
        model.addAttribute("status", STATUS_SUCCEED);

        return "status";
    }

    @RequestMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/add/user")
    public String addUser(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        try {
            DeliveryDB deliveryDB = new DeliveryDB();
            boolean status = deliveryDB.addUser(id, pw, name, phone, email, address);

            if (status)
                model.addAttribute("status", STATUS_SUCCEED);
            else
                model.addAttribute("status", STATUS_FAILED);
        } catch(Exception e) {
            System.out.println("An exception occured while connecting sql server");
        }
        
        return "status";
    }
}