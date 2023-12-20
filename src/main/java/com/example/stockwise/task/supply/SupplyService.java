package com.example.stockwise.task.supply;

import java.util.Set;

public interface SupplyService {
    void addSupply(Supply supply, Long id) throws Exception;

    Set<Supply> getAllSuppliesByWarehouseId(Long id);

    void completeSupply(Long taskId, Long id) throws Exception;

    void deleteSupply(Long taskId, Long id) throws Exception;
}
