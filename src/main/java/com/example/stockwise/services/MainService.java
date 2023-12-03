package com.example.stockwise.services;

import com.example.stockwise.user.UserService;
import com.example.stockwise.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class MainService {

    UserService userService;

    public MainService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())){
            String username =((UserDetails) authentication.getPrincipal()).getUsername();
            return userService.findUserByUsername(username);
        }
        return Optional.empty();
    }
}
