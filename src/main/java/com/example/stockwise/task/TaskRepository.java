package com.example.stockwise.task;

import com.example.stockwise.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Set<Task> findAllByWarehouseId(Long warehouse_id);

    Set<Task> findAllByWarehouseIdAndCompletedOrderByDeadlineAsc(Long warehouse_id, boolean done);

    Set<Task> findAllByAssignee(User assignee);
}
