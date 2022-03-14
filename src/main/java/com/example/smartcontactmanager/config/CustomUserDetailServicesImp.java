package com.example.smartcontactmanager.config;

import com.example.smartcontactmanager.dao.UserRepo;
import com.example.smartcontactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

public class CustomUserDetailServicesImp implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user = this.userRepo.getUserByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException(String.format("User %s could  not found ",user));
        }

        return new CustomUserDetail(user);
    }
}