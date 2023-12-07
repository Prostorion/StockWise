package com.example.stockwise.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface MainService {
    Optional<UserDetails> getUserDetails();
}
