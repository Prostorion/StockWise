package com.example.stockwise.task.supply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    Set<Supply> findAllByWarehouseIdOrderByDeadline(Long warehouse_id);
}
