package com.example.stockwise.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Set<Item> findAllByWarehouseId(Long warehouse_id);

    Set<Item> findAllByWarehouseIdOrderByNameAsc(Long warehouse_id);
    Set<Item> findAllByWarehouseIdOrderByMeasurementAsc(Long id);
    Set<Item> findAllByWarehouseIdOrderByAmountAsc(Long id);
    Set<Item> findAllByWarehouseIdOrderByRackAsc(Long id);
    Set<Item> findAllByWarehouseIdOrderByNameDesc(Long warehouse_id);


    Set<Item> findAllByWarehouseIdOrderByMeasurementDesc(Long id);

    Set<Item> findAllByWarehouseIdOrderByAmountDesc(Long id);

    Set<Item> findAllByWarehouseIdOrderByRackDesc(Long id);
}
