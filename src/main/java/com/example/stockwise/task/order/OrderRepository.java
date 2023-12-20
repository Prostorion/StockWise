package com.example.stockwise.task.order;

import com.example.stockwise.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Set<Order> findAllByWarehouseId(Long warehouse_id);

    Set<Order> findAllByWarehouseIdOrderByDeadlineAsc(Long warehouse_id);

    Set<Order> findAllByAssignee(User assignee);
}
