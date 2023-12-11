package com.example.stockwise.task;

import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/warehouses/{id}/tasks")
public class TaskController {
    UserService userService;

    WarehouseService warehouseService;

    TaskService taskService;

    public TaskController(UserService userService, WarehouseService warehouseService, TaskService taskService) {
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.taskService = taskService;
    }

    @GetMapping()
    public String getAllTasks(@PathVariable Long id, Model model) throws Exception {
        User user = userService.getUser().orElseThrow(() -> new UsernameNotFoundException("there is no user"));
        model.addAttribute(user);
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(taskService.getAllTasksByWarehouseId(id));
        return "warehouse/tasks";
    }
}
