package com.example.stockwise.user;

import com.example.stockwise.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {

    void saveUser(User user) throws Exception;

    void saveAdmin(User user) throws Exception;

    void saveManager(User user) throws Exception;
    void saveWorker(User user) throws Exception;

    Optional<User> findUserByUsername(String username);
}
