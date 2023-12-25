package com.example.stockwise.task;

import com.example.stockwise.graph.Edge;

import java.util.List;

public interface TaskService {
    List<List<Edge>> getPath(Long taskId, Long id, boolean isOrder) throws Exception;
}
