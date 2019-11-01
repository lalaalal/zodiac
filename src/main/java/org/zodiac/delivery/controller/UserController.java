package org.zodiac.delivery.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zodiac.delivery.DeliveryDB;
import org.zodiac.delivery.model.User;

@Controller
public class UserController {

    private static final Boolean STATUS_SUCCEED = true;
    private static final Boolean STATUS_FAILED = false;

    public static void checkUserLoggedIn(Model model, HttpSession session) {
        if (isUserLoggedIn(session))
            model.addAttribute("login", true);
        else model.addAttribute("login", false);
    }

    public static boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

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

    private User checkUser(String id, String pw) {
        try {
            DeliveryDB db = new DeliveryDB();

            return db.findUserByIdPw(id, pw);
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("SQL Error");
        }
        return null;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkUserLoggedIn(model, session);
        if (isUserLoggedIn(session)) {
            model.addAttribute("status", false);
            model.addAttribute("text", "이미 로그인되었습니다.");
            return "status";
        }

        return "login";
    }

    @PostMapping("/signin")
    public String signIn(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkUserLoggedIn(model, session);
        if (isUserLoggedIn(session)) {
            model.addAttribute("status", false);
            model.addAttribute("text", "이미 로그인되었습니다.");
            return "status";
        }

        String userId = request.getParameter("id");
        String userPw = request.getParameter("pw");
        userPw = SHA256(userPw);

        User user = checkUser(userId, userPw);
        if (user != null) {
            session.setAttribute("user", user);
            model.addAttribute("status", true);
        } else {
            model.addAttribute("status", false);
        }

        return "status";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkUserLoggedIn(model, session);
        if (!isUserLoggedIn(session)) {
            model.addAttribute("status", false);
            model.addAttribute("text", "로그인되지 않았습니다.");
            return "status";
        }

        session.removeAttribute("user");
        model.addAttribute("status", STATUS_SUCCEED);

        return "status";
    }

    @RequestMapping("/signup")
    public String signUp(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkUserLoggedIn(model, session);
        if (isUserLoggedIn(session)) {
            model.addAttribute("status", false);
            model.addAttribute("text", "이미 로그인되었습니다.");
            return "status";
        }

        return "signup";
    }

    @PostMapping("/add/user")
    public String addUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkUserLoggedIn(model, session);
        if (isUserLoggedIn(session)) {
            model.addAttribute("status", false);
            model.addAttribute("text", "이미 로그인되었습니다.");
            return "status";
        }

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String domain = request.getParameter("domain");
        String address = request.getParameter("address");

        pw = SHA256(pw);

        try {
            DeliveryDB deliveryDB = new DeliveryDB();
            boolean status = deliveryDB.addUser(id, pw, name, phone, email + domain, address);

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