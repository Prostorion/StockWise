package com.example.stockwise.task.supply;

import com.example.stockwise.rack.RackService;
import com.example.stockwise.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouses/{id}/supplies")
public class SupplyController {
    private final SupplyService supplyService;
    private final WarehouseService warehouseService;
    private final RackService rackService;

    @GetMapping("/{taskId}/path")
    public String getPath(@PathVariable Long id, @PathVariable Long taskId, Model model) throws Exception {
        model.addAttribute("edgeLists", supplyService.getPath(taskId, id));
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(rackService.getSupplyRacks(taskId, id));
        return "warehouse/order_path";
    }
}
