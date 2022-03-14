package com.example.smartcontactmanager.controllers;


import com.example.smartcontactmanager.dao.ContactRepo;
import com.example.smartcontactmanager.dao.UserRepo;
import com.example.smartcontactmanager.entities.Contact;
import com.example.smartcontactmanager.entities.User;
import com.example.smartcontactmanager.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @ModelAttribute
    public void globalData(Model model, Principal principal){
        String username = principal.getName();
        User user = this.userRepo.getUserByUsername(username);
        model.addAttribute("user",user);


    }

    @GetMapping(path = "/show-detail/{id}")
    public String getContactDetail(@PathVariable("id") Integer id, Model model, Principal principal){
        model.addAttribute("title","contact detail");

        User user = this.userRepo.getUserByUsername( principal.getName()  );
        Contact contact = this.contactRepo.findContactById(id);

        if (contact.getUser().getId()==user.getId()){
            model.addAttribute("contact",contact );
            return "user/contact_detail";
        }


        return "user/contact_list";

    }


    @GetMapping("/dashboard")
    public String index(Model model){

        model.addAttribute("title","User dashboard");
        return "user/user_dashboard";

    }


    @GetMapping(path = "/add-contact")
    public String addContactForm(Model model){
        model.addAttribute("title","add contact form");
        model.addAttribute("contact",new Contact()  );

        return "user/add_contact_form";
    }

    @GetMapping(path = "/contact-list/{pageNumber}")
    public String contactList(@PathVariable("pageNumber") Integer currentPage,  Model model, Principal principal){
        model.addAttribute("title","add contact form");
        String username = principal.getName();
        User user = this.userRepo.getUserByUsername(username);
        final int PER_PAGE=10;
        Pageable pageable= PageRequest.of(currentPage,PER_PAGE);
        Page<Contact> contacts = this.contactRepo.findAllContactsByUser(user.getId(),pageable);
        model.addAttribute("contacts",contacts);
        model.addAttribute("totalPages",contacts.getTotalPages());
        model.addAttribute("currentPage",currentPage);
        return "user/contact_list";
    }

    @GetMapping(path = "/profile")
    public String getProfile(Model model, Principal principal){
        model.addAttribute("title","your profile");
        String username = principal.getName();
        User user= this.userRepo.getUserByUsername(username);
        model.addAttribute("loggedInUser",user);
        return "user/profile";
    }

    @PostMapping(path = "/process-contact-form")
    public String processForm(@ModelAttribute Contact contact , @RequestParam("fileUpload")MultipartFile file, Principal principal, RedirectAttributes redirectAttributes){

        try {

            String name = principal.getName();
            User user = this.userRepo.getUserByUsername(name);

            /**
             * fileUpload
             */
            if (file.isEmpty()){
                System.out.println("file is empty");
                contact.setImgUrl("default.png");
            }else {
                File savefile = new ClassPathResource("/static/images/").getFile();
                Files.copy(file.getInputStream(), Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename()) , StandardCopyOption.REPLACE_EXISTING);
                contact.setImgUrl(file.getOriginalFilename());
            }
            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepo.save(user);

//            System.out.println("contact added "+contact);
//            System.out.println("user "+user);

            redirectAttributes.addFlashAttribute("message","Contact has been saved");

        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error while uploading file"+e.getMessage() );
            e.printStackTrace();
        }

        return "redirect:/user/add-contact";
//        return "user/add_contact_form";

    }

    @GetMapping(path = "/edit-contact/{id}")
    public String openContactForm(@PathVariable("id") Integer id, Model model, Principal principal){
        model.addAttribute("title","edit contact form");
        String username = principal.getName();
        User user = this.userRepo.getUserByUsername(username);
        Contact contact = this.contactRepo.findContactById(id);
         if (contact.getUser().getId()==user.getId()){
             model.addAttribute("contact",contact);
              return "user/edit_contact_form";
         }
        return "user/contact_list";
    }


    @PostMapping(path = "/update-contact")
    public String updateContact(
            @ModelAttribute Contact contact,
            @RequestParam("fileUpload")MultipartFile file,
//            @RequestParam("contactId")Long contactId,
            Principal principal,
            Model model,
            HttpSession session
    ){
         try {

           /*  System.out.println("contact id "+contact.getId());
             System.out.println("contact name "+contact.getName());
             System.out.println("contact img "+contact.getImgUrl());*/
            Contact oldContact = this.contactRepo.findById(contact.getId() ).get();

        if (file.isEmpty()){
                System.out.println("file is empty");
                contact.setImgUrl(oldContact.getImgUrl());
            }else {
            // delete old file
            boolean delete = new File(new ClassPathResource("/static/images/").getFile(), oldContact.getImgUrl()).delete();

            // updated file
                File savefile = new ClassPathResource("/static/images/").getFile();
                Files.copy(file.getInputStream(), Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename()) , StandardCopyOption.REPLACE_EXISTING);
                contact.setImgUrl(file.getOriginalFilename());
            }

             User user = this.userRepo.getUserByUsername(principal.getName());
             contact.setUser(user);
             this.contactRepo.save(contact);
//            session.setAttribute("message", new Message("Contact has been updated","alert alert-success"));

        }
        catch (Exception e){
            session.setAttribute("message", new Message("Error while uploading file"+e.getMessage(),"alert alert-danger"));

            e.printStackTrace();
        }


         return "redirect:/user/contact-list/0";
    }


    @GetMapping(path = "/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer id, Principal principal, HttpSession session){
        try {
            String username = principal.getName();
            User user = this.userRepo.getUserByUsername(username);
            Contact contact = this.contactRepo.findContactById(id);
            if (contact.getUser().getId()==user.getId()){
                contact.setUser(null);
                this.contactRepo.delete(contact);
                new File(new ClassPathResource("/static/images/").getFile(), contact.getImgUrl()).delete();
//                session.setAttribute("message", new Message("Contact has been deleted","alert alert-success"));
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("Error while deleting contact"+e.getMessage(),"alert alert-danger"));
        }

        return "redirect:/user/contact-list/0";
    }


    @GetMapping(path = "/settings")
    public String settings(Model model, HttpSession session){
        model.addAttribute("title","your settings");
        return "user/settings";
    }

    @PostMapping(path = "/change-password")
    public String changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){
        String username = principal.getName();
        User currentUser = this.userRepo.getUserByUsername(username);
        if (this.bCryptPasswordEncoder.matches(oldPassword,currentUser.getPassword())){
            if (newPassword.equals(confirmPassword)){
                currentUser.setPassword(  this.bCryptPasswordEncoder.encode(newPassword) );
                this.userRepo.save(currentUser);
                redirectAttributes.addFlashAttribute("message","Password has been changed");
            }else {
              redirectAttributes.addFlashAttribute("error", "New password and confirm password does not match");
            }
        }else {

            redirectAttributes.addFlashAttribute("error", "Old password is not correct");


        }
        return "redirect:/user/settings";
    }


}
