package com.example.smartcontactmanager.controllers;


import com.example.smartcontactmanager.dao.ContactRepo;
import com.example.smartcontactmanager.dao.UserRepo;
import com.example.smartcontactmanager.entities.Contact;
import com.example.smartcontactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContactRepo contactRepo;


    @RequestMapping(path = "/search/keyword", method = RequestMethod.POST)
    public ResponseEntity<?> search(@RequestBody String keyword, Principal principal) {

        User user = this.userRepo.getUserByUsername(principal.getName());
        List<Contact> contacts= this.contactRepo.findByNameContainingAndUser(keyword,user);

        return ResponseEntity.ok(contacts);
    }

}
