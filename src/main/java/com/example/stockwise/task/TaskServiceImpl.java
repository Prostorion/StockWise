package com.example.stockwise.task;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Set<Task> getAllTasksByWarehouseId(Long id) {
        return taskRepository.findAllByWarehouseId(id);
    }
}
