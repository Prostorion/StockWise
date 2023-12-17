package com.example.stockwise.task;

import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

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
        model.addAttribute(warehouseService.getWarehouseById(id));
        model.addAttribute(taskService.getAllTasksByWarehouseId(id));
        model.addAttribute(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        return "warehouse/tasks";
    }

    @GetMapping("/new")
    public String getNewTaskForm(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute(userService.getWarehouseUsers(id));
        return "warehouse/tasks_new";
    }

    @PostMapping()
    public ResponseEntity<?> addTask(@PathVariable Long id, @RequestBody Task task) {
        try {
            taskService.addTask(task, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeTAsk(@PathVariable Long id, @PathVariable Long taskId) {
        try {
            taskService.completeTask(taskId, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("");
    }
}
