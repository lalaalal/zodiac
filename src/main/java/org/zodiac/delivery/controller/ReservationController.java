package org.zodiac.delivery.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zodiac.delivery.Hmac;
import org.zodiac.delivery.SHA256Util;

@Controller
public class ReservationController {
    public static final String MESSAGE = "http://bontin.ga/check/delivery?no=%d&senderid=%s&hash=%s";

    public static void sendMessage(String senderPhone, String message) throws Exception {
        String targetUrl = "http://api.solapi.com/messages/v4/send";
        String parameters = String.format("{\"message\":{\"to\":\"%s\",\"from\":\"01066259880\",\"text\":\"%s\"}}", senderPhone, message);
        System.out.println(parameters);

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String datetime = dateFormat.format(new Date());
        String salt = SHA256Util.generateSalt();
        String signature = Hmac.hget(datetime + salt);
        String auth = String.format("HMAC-SHA256 apiKey=NCSUKGEXI1IH3KGZ, date=%s, salt=%s, signature=%s", datetime, salt, signature);

        con.setRequestProperty("Authorization", auth);
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = in.readLine()) != null) {
        response.append(line);
        }
        in.close();

        System.out.println("HTTP response code : " + responseCode);
        System.out.println("HTTP body : " + response.toString());
    }

    @GetMapping("/delivery/reservation")
    public String reservation(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserController.checkUserLoggedIn(model, session);
        
        return "reservation";
    }
}