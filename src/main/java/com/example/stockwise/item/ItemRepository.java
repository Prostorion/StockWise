package com.example.stockwise.item;

import com.example.stockwise.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Set<Item> findAllByRackWarehouseIdAndTask(Long rack_warehouse_id, Task task);

}
