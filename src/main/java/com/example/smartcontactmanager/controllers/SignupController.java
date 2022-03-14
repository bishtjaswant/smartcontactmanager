package com.example.smartcontactmanager.controllers;


import com.example.smartcontactmanager.dao.UserRepo;
import com.example.smartcontactmanager.entities.User;
import com.example.smartcontactmanager.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class SignupController {

    private final String APP_NAME="Smart Contact  Manager";
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(path = "/signup")
    public String signup(Model model) {
        model.addAttribute("title","Register yourself ::"+APP_NAME);
        model.addAttribute("user", new User() );
        return "signup_page";
    }

    @PostMapping(path = "/process")
    public String process(@ModelAttribute("user") User user, @RequestParam(value = "terms", defaultValue = "false") boolean terms, Model model ,@RequestParam("userFile") MultipartFile file, BindingResult bindingResult, RedirectAttributes redirectAttributes ) {
//        if (bindingResult.hasErrors()){
//            System.out.println(bindingResult);
//            return "signup_page";
//        }
         try {
             if(!terms){
                throw new Exception("Please accept term and condition");
             }
             user.setEnabled(true);
             if (!file.isEmpty()) {
                 File getFile = new ClassPathResource("/static/images/").getFile();
                 Files.copy(file.getInputStream(), Paths.get(getFile.getAbsolutePath()+File.separator+file.getOriginalFilename()));
                 user.setImgUrl(file.getOriginalFilename());
             } else {
                 user.setImgUrl("default.png");
             }
             user.setRole("ROLE_USER");
             user.setPassword( this.passwordEncoder.encode(user.getPassword()  ) );

             User save = this.userRepo.save(user);
             redirectAttributes.addFlashAttribute("message", new Message("User registered","alert alert-success"));
             return "login";
         } catch (Exception e){
             e.printStackTrace();
             model.addAttribute("user", new User() );
             redirectAttributes.addFlashAttribute("error", new Message("something went wrong... "+e.getMessage(),"alert alert-danger"));
             return "redirect:/signup";
         }

    }

    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title","Login form ::"+APP_NAME);
        return "login";
    }
}
