package com.example.stockwise.graph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VertexRepository extends JpaRepository<Vertex, Long> {

    void deleteAllByWarehouseId(Long warehouse_id);

    Set<Vertex> findAllByWarehouseId(Long warehouse_id);

}
