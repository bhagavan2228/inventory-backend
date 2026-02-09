package com.inventory.inventory_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management_system.entity.Inventory;
import com.inventory.inventory_management_system.repository.InventoryRepo;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    private final InventoryRepo inventoryRepo;

    public InventoryController(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    // ✅ Get inventory for ALL products (Frontend main use)
    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryRepo.findAll();
    }

    // ✅ Get inventory by product id (optional but useful)
    @GetMapping("/product/{productId}")
    public Inventory getInventoryByProductId(@PathVariable Long productId) {
        return inventoryRepo.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }
}
