package com.example.stockwise.user;

import com.example.stockwise.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Set<User> findAllByWarehousesIdOrderByFirstname(Long warehouses_id);

}
