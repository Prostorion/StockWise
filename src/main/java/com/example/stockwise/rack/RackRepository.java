package com.example.stockwise.rack;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RackRepository extends JpaRepository<Rack, Long> {

    Optional<Rack> findRackByNumber(Long number);

    Optional<Rack> findRackByNumberAndWarehouseId(Long number, Long warehouse_id);

    Optional<Rack> findRackById(Long id);
}
