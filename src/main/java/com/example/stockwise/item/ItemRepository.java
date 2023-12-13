package com.example.stockwise.item;

import com.example.stockwise.task.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.net.Socket;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i LEFT JOIN i.rack r WHERE r.warehouse.id = :warehouse_id AND i.task.completed = :task_completed")
    Set<Item> findAllByRackWarehouseIdAndTaskCompleted(@Param("warehouse_id") Long warehouse_id, @Param("task_completed") boolean task_completed, Sort sort);

}
