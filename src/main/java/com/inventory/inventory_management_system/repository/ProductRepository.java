package com.inventory.inventory_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management_system.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 🔍 Search by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // 🧩 Filter by category
    List<Product> findByCategoryId(Long categoryId);

    // 💰 Filter by price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    // 🔍🧩 Combined: name + category
    List<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, Long categoryId);
}
