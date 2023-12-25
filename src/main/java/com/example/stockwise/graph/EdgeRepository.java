package com.example.stockwise.graph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {

    List<Edge> findAllByFirstVertexWarehouseIdOrSecondVertexWarehouseIdOrderById(Long firstVertex_warehouse_id, Long secondVertex_warehouse_id);

    List<Edge> findAllByFirstVertexIdAndSecondVertexId(Long firstVertex_id, Long secondVertex_id);
}
