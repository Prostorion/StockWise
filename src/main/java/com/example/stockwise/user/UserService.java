package com.example.stockwise.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {

    void addUser(User user) throws Exception;

    void addAdmin(User user) throws Exception;

    void addManager(User user) throws Exception;

    void addWorker(User user) throws Exception;

    Optional<User> findUserByUsername(String username);

    boolean isWarehouseAccessibleByUser(String username, Long requestedWarehouseId);

    public Optional<User> getUser();

    void updateUser(User user) throws Exception;
}
