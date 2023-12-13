package com.example.stockwise.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;


public interface UserService {

    void addAdmin(User user, PasswordEncoder passwordEncoder) throws Exception;

    void addManager(User user, PasswordEncoder passwordEncoder) throws Exception;

    void addWorker(User user, PasswordEncoder passwordEncoder) throws Exception;

    Optional<User> findUserByUsername(String username);

    boolean isWarehouseAccessibleByUser(String username, Long requestedWarehouseId);

    Optional<User> getUser();

    void updateUser(User user) throws Exception;

    void addWorkerToWarehouse(User user, Long id, PasswordEncoder passwordEncoder) throws Exception;

    Set<User> getWarehouseUsers(Long id) throws Exception;

    Optional<User> getUserByUsername(String username);

    void deleteWorker(Long userId, Long id) throws Exception;

    void updateCurrentUser(User user) throws Exception;
}
