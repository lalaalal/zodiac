package org.zodiac.delivery.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {
    public static final String MESSAGE = "미르택배 : %s\n보내는 사람: %s\n보내는 사람 전화번호 : %s";

    public static void snedMessage(String phone, String message) throws Exception {
        String targetUrl = "http://api.solapi.com/messages/v4/send";
        String parameters = String.format("{\"message\":{\"to\":\"%s\",\"from\":\"01066259880\",\"text\":\"%s\"}}", phone, message);

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");

        con.setRequestProperty("Authorization", "HMAC-SHA256 apiKey=NCSUKGEXI1IH3KGZ, date=2019-07-01T00:41:48Z, salt=jqsba2jxjnrjor, signature=1779eac71a24cbeeadfa7263cb84b7ea0af1714f5c0270aa30ffd34600e363b4");
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