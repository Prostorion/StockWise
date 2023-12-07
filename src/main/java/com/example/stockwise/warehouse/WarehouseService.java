package com.example.stockwise.warehouse;

import com.example.stockwise.user.User;

import java.util.Set;

public interface WarehouseService {
    void addWarehouse(Warehouse warehouse) throws Exception;

    Set<Warehouse> getUserWarehouses(User user);

    void deleteWarehouseById(Long id) throws Exception;

    Warehouse getWarehouseById(Long id) throws Exception;

    void updateWarehouse(Long id, Warehouse warehouse) throws Exception;
}
