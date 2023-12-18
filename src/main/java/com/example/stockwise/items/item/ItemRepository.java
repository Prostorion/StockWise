package com.example.stockwise.items.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i LEFT JOIN i.rack r WHERE r.warehouse.id = :warehouse_id")
    Set<Item> findAllByRackWarehouseIdAndTaskCompleted(@Param("warehouse_id") Long warehouse_id, Sort sort);

}
