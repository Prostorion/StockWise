package com.example.stockwise.items.itemToExport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {

}
