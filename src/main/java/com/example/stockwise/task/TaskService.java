package com.example.stockwise.task;

import java.util.Set;

public interface TaskService {

    Set<Task> getAllTasksByWarehouseId(Long id);

    void addTask(Task task, Long id) throws Exception;

    //void deleteTask(Long taskId, Long warehouseId) throws Exception;

    void completeTask(Long taskId, Long id) throws Exception;
}
