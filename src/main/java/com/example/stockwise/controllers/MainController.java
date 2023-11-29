package com.example.stockwise.controllers;

import com.example.stockwise.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1")
public class MainController {


    @GetMapping()
    @ResponseBody
    public String mainPage(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "It works "+ userDetails.getUsername() + " " + userDetails.getPassword();
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

}
