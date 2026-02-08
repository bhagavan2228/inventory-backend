package com.inventory.inventory_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory_management_system.model.Product;
import com.inventory.inventory_management_system.model.StockMovement;
import com.inventory.inventory_management_system.repository.ProductRepository;
import com.inventory.inventory_management_system.repository.StockMovementRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public ProductController(ProductRepository productRepository,
                             StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    // ✅ Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ SEARCH & FILTER PRODUCTS (DAY 3 – STEP 3.5 🔥)
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {

        if (name != null && categoryId != null) {
            return productRepository
                    .findByNameContainingIgnoreCaseAndCategoryId(name, categoryId);
        }

        if (name != null) {
            return productRepository.findByNameContainingIgnoreCase(name);
        }

        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        }

        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }

        return productRepository.findAll();
    }

    // ✅ Add new product + log INITIAL_STOCK
    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product) {

        Product savedProduct = productRepository.save(product);

        StockMovement movement = new StockMovement(
                savedProduct,
                savedProduct.getQuantity(),
                "IN",
                "INITIAL_STOCK"
        );

        stockMovementRepository.save(movement);

        return savedProduct;
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    // ✅ STOCK IN (Increase quantity + log)
    @PostMapping("/{id}/stock-in")
    public Product stockIn(
            @PathVariable Long id,
            @RequestParam int amount
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity(product.getQuantity() + amount);
        Product updated = productRepository.save(product);

        StockMovement movement = new StockMovement(
                updated,
                amount,
                "IN",
                "STOCK_IN"
        );

        stockMovementRepository.save(movement);

        return updated;
    }

    // ✅ STOCK OUT (Decrease quantity + log SALE)
    @PostMapping("/{id}/stock-out")
    public Product stockOut(
            @PathVariable Long id,
            @RequestParam int amount
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < amount) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - amount);
        Product updated = productRepository.save(product);

        StockMovement movement = new StockMovement(
                updated,
                -amount,
                "OUT",
                "SALE"
        );

        stockMovementRepository.save(movement);

        return updated;
    }
}
