package com.example.smartcontactmanager.controllers;


import com.example.smartcontactmanager.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    /** get the random otp and send it to the user*/
    Random random=new Random(1000);


    @GetMapping("/forgotPassword")
    public String forgot_password(Model model) {
        model.addAttribute("title", "Forgot Password");
        return "forgot_password";
    }

    @PostMapping("/sentOtp")
    public String sentOtp(@RequestParam("email") String email, HttpSession session) {
        int otp = random.nextInt(999999);
        String to= email;
        String subject="OTP for password reset from Smart Contact Manager";
        String message="Your OTP is "+otp;
        boolean sendEmail = EmailService.sendEmail(to, subject, message);
        if (sendEmail) {
            session.setAttribute("message", "Enter the OTP sent to your email address");
            System.out.println("Email sent successfully");
            return "send_otp";
        }else {
            session.setAttribute("error", "Email not sent");
            return "send_otp";
        }

    }



}
