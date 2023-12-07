package com.example.stockwise.services;

import com.example.stockwise.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {

    UserService userService;

    public MainServiceImpl(UserService userService) {
        this.userService = userService;
    }


    public Optional<UserDetails> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of((UserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
