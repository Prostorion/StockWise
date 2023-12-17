package com.example.stockwise.warehouse;

import com.example.stockwise.user.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByName(String name);

    Set<Warehouse> findAllByUserSetContainsOrderByName(User user);

    @Query("SELECT w FROM Warehouse w LEFT JOIN FETCH w.userSet u WHERE u.username = :userUsername")
    Set<Warehouse> findAllByUserUsername(@Param("userUsername") String username);

    void deleteById(@NonNull Long id);

}
