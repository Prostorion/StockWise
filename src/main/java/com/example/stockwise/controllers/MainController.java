package com.example.stockwise.controllers;

import com.example.stockwise.services.MainService;
import com.example.stockwise.user.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/v1")
public class MainController {
    MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping()
    public String mainPage(Model model) {
        Optional<User> userOptional = mainService.getUser();
        if (userOptional.isPresent()){
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
