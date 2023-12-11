package com.example.stockwise.task;

import java.util.Set;

public interface TaskService {

    Set<Task> getAllTasksByWarehouseId(Long id);
}
