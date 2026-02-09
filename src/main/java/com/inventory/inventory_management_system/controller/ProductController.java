package com.inventory.inventory_management_system.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management_system.dto.ProductRequest;
import com.inventory.inventory_management_system.dto.ProductResponse;
import com.inventory.inventory_management_system.dto.StockMovementResponse;
import com.inventory.inventory_management_system.entity.Inventory;
import com.inventory.inventory_management_system.model.Product;
import com.inventory.inventory_management_system.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 👀 ADMIN + STAFF + VIEWER
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','VIEWER')")
    @GetMapping
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productService.getProductsPaged(pageable);
    }

    // 👀 ADMIN + STAFF + VIEWER
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','VIEWER')")
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return productService.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse addProduct(@Valid @RequestBody ProductRequest request) {
        return productService.addProductResponse(request);
    }

    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // 🔐 ADMIN + STAFF
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping("/{id}/stock-in")
    public Product stockIn(@PathVariable Long id,
                           @RequestParam int amount) {
        return productService.stockIn(id, amount);
    }

    // 🔐 ADMIN + STAFF
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PostMapping("/{id}/stock-out")
    public Product stockOut(@PathVariable Long id,
                            @RequestParam int amount) {
        return productService.stockOut(id, amount);
    }

    // 👀 ADMIN + STAFF + VIEWER
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','VIEWER')")
    @GetMapping("/{id}/stock-history")
    public List<StockMovementResponse> getStockHistory(@PathVariable Long id) {
        return productService.getStockHistoryDto(id);
    }

    // 👀 ADMIN + STAFF + VIEWER
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','VIEWER')")
    @GetMapping("/low-stock")
    public List<Inventory> getLowStockProducts(
            @RequestParam(defaultValue = "5") int threshold) {
        return productService.getLowStockProducts(threshold);
    }
}
