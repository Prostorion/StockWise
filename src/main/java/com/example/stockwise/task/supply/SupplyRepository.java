package com.example.stockwise.task.supply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    Set<Supply> findAllByWarehouseIdOrderByDeadline(Long warehouse_id);

    void deleteAllByWarehouseId(Long warehouse_id);
}
