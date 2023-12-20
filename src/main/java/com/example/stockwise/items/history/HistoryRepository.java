package com.example.stockwise.items.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryItem, Long> {

    Set<HistoryItem> findAllByWarehouseIdOrderByTimeOfAdditionDesc(Long warehouse_id);
}
