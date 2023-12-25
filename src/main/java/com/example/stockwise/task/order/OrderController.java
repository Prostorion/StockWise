package com.example.stockwise.task.order;

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
@RequestMapping("/api/v1/warehouses/{id}/orders")
public class OrderController {
    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final RackService rackService;

    @GetMapping("/{taskId}/path")
    public String getPath(@PathVariable Long id, @PathVariable Long taskId, Model model) throws Exception {
        model.addAttribute("edgeLists", orderService.getPath(taskId, id));
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(rackService.getOrderRacks(taskId));
        return "warehouse/order_path";
    }
}
