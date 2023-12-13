package com.example.stockwise.controllers;

import com.example.stockwise.services.MainService;
import com.example.stockwise.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/v1")
public class MainController {
    MainService mainService;
    UserService userService;

    public MainController(MainService mainService, UserService userService) {
        this.mainService = mainService;
        this.userService = userService;
    }

    @GetMapping()
    public String mainPage(Model model) {
        Optional<UserDetails> userOptional = mainService.getUserDetails();
        if (userOptional.isPresent()) {
            model.addAttribute(userOptional.get());
            return "logged_main";
        }

        return "main";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }



}
