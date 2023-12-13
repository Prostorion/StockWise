package com.example.stockwise.task;

import com.example.stockwise.item.Item;
import com.example.stockwise.item.ItemService;
import com.example.stockwise.rack.Rack;
import com.example.stockwise.rack.RackRepository;
import com.example.stockwise.user.User;
import com.example.stockwise.user.UserService;
import com.example.stockwise.warehouse.Warehouse;
import com.example.stockwise.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    TaskTypeRepository taskTypeRepository;
    WarehouseService warehouseService;
    UserService userService;
    RackRepository rackRepository;

    ItemService itemService;

    public TaskServiceImpl(TaskRepository taskRepository, TaskTypeRepository taskTypeRepository, WarehouseService warehouseService, UserService userService, RackRepository rackRepository, ItemService itemService) {
        this.taskRepository = taskRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.warehouseService = warehouseService;
        this.userService = userService;
        this.rackRepository = rackRepository;
        this.itemService = itemService;
    }

    @Override
    public Set<Task> getAllTasksByWarehouseId(Long id) {
        return taskRepository.findAllByWarehouseIdAndCompletedOrderByDeadlineAsc(id, false);
    }

    @Override
    public void addTask(Task task, Long id) throws Exception {
        setWarehouse(task, id);
        setTaskType(task);
        setAssignee(task, id);
        setAuthor(task);
        setItems(task);
        setCreationDateAndDeadline(task);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId, Long warehouseId) throws Exception {
        Task task = getTaskById(taskId, warehouseId);
        if (task.isCompleted()){
            throw new Exception("Task is already completed");
        }
        Set<Item> items = task.getItems();
        for (Item item : items) {
            itemService.deleteItem(item);
        }
        taskRepository.delete(task);
    }

    private Task getTaskById(Long taskId, Long warehouseId) throws Exception {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task is not found"));
        if (!task.getWarehouse().getId().equals(warehouseId)){
            throw new Exception("Task is not found in this warehouse");
        }
        return task;
    }

    @Override
    public void completeTask(Long taskId, Long id) throws Exception {
        Task task = getTaskById(taskId, id);
        if (task.isCompleted()){
            throw new Exception("task is already completed");
        }

        task.setCompleted(true);
        taskRepository.save(task);
    }

    private static void setCreationDateAndDeadline(Task task) throws Exception {

        LocalDateTime deadline = task.getDeadline();
        if (deadline.isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new Exception("The deadline has passed");
        }
        task.setCreationDate(LocalDateTime.now());

    }

    private void setTaskType(Task task) throws Exception {
        TaskType taskType = taskTypeRepository.findByType(task.getTaskType().getType()).orElseThrow(() -> new Exception("Task is not order or supply"));
        task.setTaskType(taskType);
    }

    private void setWarehouse(Task task, Long id) throws Exception {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        task.setWarehouse(warehouse);
    }

    private void setItems(Task task) throws Exception {
        Set<Item> items = task.getItems();
        for (Item item : items) {
            Rack rack = rackRepository.findRackByNumber(
                    item.getRack()
                            .getNumber()).orElseThrow(() -> new Exception("Rack is not found"));
            item.setRack(rack);
            item.setTask(task);
        }
    }

    private void setAuthor(Task task) throws Exception {
        User author = userService.getUser().orElseThrow(() -> new Exception("User not found (author)"));
        task.setAuthor(author);
    }

    private void setAssignee(Task task, Long id) throws Exception {
        User assignee = userService.getUserByUsername(task.getAssignee().getUsername()).orElseThrow(() -> new Exception("User not found (assignee)"));
        if (!userService.getWarehouseUsers(id).contains(assignee)) {
            throw new Exception("User is not allowed to warehouse");
        }
        task.setAssignee(assignee);
    }
}
