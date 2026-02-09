package com.inventory.inventory_management_system.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management_system.entity.Inventory;
import com.inventory.inventory_management_system.service.ProductService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ProductService productService;

    public ReportController(ProductService productService) {
        this.productService = productService;
    }

    // 📉 LOW STOCK REPORT
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/low-stock")
    public List<Inventory> lowStock(
            @RequestParam(defaultValue = "5") int threshold) {
        return productService.getLowStockProducts(threshold);
    }

    // 💰 TOTAL INVENTORY VALUE
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/inventory-value")
    public double totalInventoryValue() {
        return productService.getTotalInventoryValue();
    }

    // 🚫 ZERO STOCK PRODUCTS
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/zero-stock")
    public List<Inventory> zeroStockProducts() {
        return productService.getZeroStockProducts();
    }
}
