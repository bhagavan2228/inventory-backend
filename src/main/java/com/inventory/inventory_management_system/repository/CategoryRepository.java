package com.inventory.inventory_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory_management_system.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
