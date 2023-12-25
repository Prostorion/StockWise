package com.example.stockwise.rack;

import java.util.List;

public interface RackService {
    Rack getRack(Long id, Long rackNumber) throws Exception;

    List<Rack> getOrderRacks(Long taskId);

    List<Rack> getSupplyRacks(Long taskId, Long warehouse_id);
}
