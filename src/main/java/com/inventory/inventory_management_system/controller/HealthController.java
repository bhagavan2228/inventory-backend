package com.inventory.inventory_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Inventory Management System is running!";
    }
}
