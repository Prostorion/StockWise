package com.example.stockwise.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Set<Task> findAllByWarehouseId(Long warehouse_id);
}
