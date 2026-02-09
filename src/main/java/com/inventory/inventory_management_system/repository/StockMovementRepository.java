package com.inventory.inventory_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management_system.model.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // ✅ DAY 10 – Stock history for a product (latest first)
    List<StockMovement> findByProductIdOrderByTimestampDesc(Long productId);
}
