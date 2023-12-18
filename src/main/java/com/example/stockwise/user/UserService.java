package com.example.stockwise.user;

import java.util.Optional;
import java.util.Set;


public interface UserService {

    void addAdmin(User user) throws Exception;

    void addManager(User user) throws Exception;

    void addWorker(User user) throws Exception;

    Optional<User> findUserByUsername(String username);

    boolean isWarehouseAccessibleByUser(String username, Long requestedWarehouseId);

    User getUser() throws Exception;

    void updateUser(User user) throws Exception;

    void addWorkerToWarehouse(User user, Long id) throws Exception;

    Set<User> getWarehouseUsers(Long id) throws Exception;

    Optional<User> getUserByUsername(String username);

    void deleteWorker(Long userId, Long id) throws Exception;

    void updateCurrentUser(User user) throws Exception;

}
