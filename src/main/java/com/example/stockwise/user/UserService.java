package com.example.stockwise.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;


public interface UserService extends UserDetailsService {

    void addUser(User user) throws Exception;

    void addAdmin(User user) throws Exception;

    void addManager(User user) throws Exception;

    void addWorker(User user) throws Exception;

    Optional<User> findUserByUsername(String username);

    boolean isWarehouseAccessibleByUser(String username, Long requestedWarehouseId);

    Optional<User> getUser();

    void updateUser(User user) throws Exception;

    void addWorkerToWarehouse(User user, Long id) throws Exception;

    Set<User> getWarehouseUsers(Long id) throws Exception;
}
