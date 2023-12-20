package com.example.stockwise.task;

import com.example.stockwise.items.item.ItemService;
import com.example.stockwise.task.order.OrderService;
import com.example.stockwise.task.supply.SupplyService;
import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouses/{id}/tasks")
public class TaskController {
    private final UserService userService;
    private final WarehouseService warehouseService;
    private final OrderService orderService;
    private final ItemService itemService;
    private final SupplyService supplyService;


    @GetMapping()
    public String getAllTasks(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(orderService.getAllOrdersByWarehouseId(id));
        model.addAttribute(supplyService.getAllSuppliesByWarehouseId(id));
        model.addAttribute(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return "warehouse/tasks";
    }

    @GetMapping("/order_new")
    public String getNewOrderForm(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(userService.getWarehouseUsers(id));
        return "warehouse/order_new";
    }

    @GetMapping("/supply_new")
    public String getNewSupplyForm(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(userService.getWarehouseUsers(id));
        model.addAttribute("itemList", itemService.getAllWarehouseItems(id));
        return "warehouse/supply_new";
    }


}
