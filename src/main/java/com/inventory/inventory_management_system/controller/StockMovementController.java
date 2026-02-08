package com.inventory.inventory_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management_system.model.StockMovement;
import com.inventory.inventory_management_system.repository.StockMovementRepository;

@RestController
@RequestMapping("/stock-history")
@CrossOrigin(origins = "http://localhost:3000")
public class StockMovementController {

    private final StockMovementRepository stockMovementRepository;

    public StockMovementController(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    // ✅ View stock history for a product
    @GetMapping("/{productId}")
    public List<StockMovement> getStockHistory(@PathVariable Long productId) {
        return stockMovementRepository.findByProductId(productId);
    }
}
