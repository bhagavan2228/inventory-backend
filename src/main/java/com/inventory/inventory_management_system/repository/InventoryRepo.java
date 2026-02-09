package com.inventory.inventory_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

import com.inventory.inventory_management_system.entity.Inventory;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    // ✅ LOW STOCK
    List<Inventory> findByQuantityLessThanEqual(int quantity);

    // ✅ ZERO STOCK
    List<Inventory> findByQuantity(int quantity);

    // ✅ TOTAL INVENTORY VALUE
    @Query("""
        SELECT SUM(p.price * i.quantity)
        FROM Inventory i
        JOIN i.product p
    """)
    Double getTotalInventoryValue();
}
