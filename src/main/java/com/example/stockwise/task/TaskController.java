package com.example.stockwise.task;

import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/api/v1/warehouses/{id}/tasks")
public class TaskController {
    UserService userService;

    WarehouseService warehouseService;

    OrderService orderService;


    public TaskController(UserService userService, WarehouseService warehouseService, OrderService orderService) {
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.orderService = orderService;
    }

    @GetMapping()
    public String getAllTasks(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(orderService.getAllOrdersByWarehouseId(id));
        model.addAttribute(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return "warehouse/tasks";
    }

    @GetMapping("/new")
    public String getNewTaskForm(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(userService.getWarehouseUsers(id));
        return "warehouse/tasks_new";
    }


}
