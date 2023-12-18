package com.example.stockwise.task;

import java.util.Set;

public interface OrderService {

    Set<Order> getAllOrdersByWarehouseId(Long id);

    void addOrder(Order order, Long warehouseId) throws Exception;

    void deleteOrder(Long orderId, Long warehouseId) throws Exception;

    void completeOrder(Long orderId, Long warehouseId) throws Exception;

    Order getOrderByIdAndWarehouseId(Long orderId, Long warehouseId) throws Exception;
}
