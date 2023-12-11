package com.example.stockwise.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Set<Item> findAllByRackWarehouseId(Long warehouse_id);

}
