package com.inventory.inventory_management_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory_management_system.dto.ProductRequest;
import com.inventory.inventory_management_system.dto.ProductResponse;
import com.inventory.inventory_management_system.dto.StockMovementResponse;
import com.inventory.inventory_management_system.entity.Inventory;
import com.inventory.inventory_management_system.excpetion.InsufficientStockException;
import com.inventory.inventory_management_system.excpetion.ResourceNotFoundException;
import com.inventory.inventory_management_system.model.Category;
import com.inventory.inventory_management_system.model.Product;
import com.inventory.inventory_management_system.model.StockMovement;
import com.inventory.inventory_management_system.repository.CategoryRepository;
import com.inventory.inventory_management_system.repository.InventoryRepo;
import com.inventory.inventory_management_system.repository.ProductRepository;
import com.inventory.inventory_management_system.repository.StockMovementRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepo inventoryRepo;
    private final StockMovementRepository stockMovementRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          InventoryRepo inventoryRepo,
                          StockMovementRepository stockMovementRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepo = inventoryRepo;
        this.stockMovementRepository = stockMovementRepository;
        this.categoryRepository = categoryRepository;
    }

    // ================= CREATE PRODUCT =================

    @Transactional
    public Product addProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(savedProduct);
        inventory.setQuantity(0);
        inventoryRepo.save(inventory);

        stockMovementRepository.save(
                new StockMovement(savedProduct, 0, "IN", "INITIAL_STOCK")
        );

        return savedProduct;
    }

    public ProductResponse addProductResponse(ProductRequest request) {
        Product product = addProduct(request);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );
    }

    // ================= SEARCH PRODUCTS (FIX #1) =================

    public List<Product> searchProducts(String name,
                                        Long categoryId,
                                        Double minPrice,
                                        Double maxPrice) {

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

    // ================= DELETE PRODUCT (FIX #2) =================

    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepo.findByProductId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        inventoryRepo.delete(inventory);
        productRepository.delete(product);
    }

    // ================= STOCK IN =================

    @Transactional
    public Product stockIn(Long productId, int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Stock-in amount must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepo.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        inventory.setQuantity(inventory.getQuantity() + amount);
        inventoryRepo.save(inventory);

        stockMovementRepository.save(
                new StockMovement(product, amount, "IN", "STOCK_IN")
        );

        return product;
    }

    // ================= STOCK OUT =================

    @Transactional
    public Product stockOut(Long productId, int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Stock-out amount must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepo.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        if (inventory.getQuantity() < amount) {
            throw new InsufficientStockException("Insufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - amount);
        inventoryRepo.save(inventory);

        stockMovementRepository.save(
                new StockMovement(product, -amount, "OUT", "SALE")
        );

        return product;
    }

    // ================= REPORTS =================

    public List<Inventory> getLowStockProducts(int threshold) {
        return inventoryRepo.findByQuantityLessThanEqual(threshold);
    }

    public List<Inventory> getZeroStockProducts() {
        return inventoryRepo.findByQuantity(0);
    }

    public double getTotalInventoryValue() {
        Double total = inventoryRepo.getTotalInventoryValue();
        return total != null ? total : 0.0;
    }

    // ================= STOCK HISTORY =================

    public List<StockMovementResponse> getStockHistoryDto(Long productId) {

        productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return stockMovementRepository
                .findByProductIdOrderByTimestampDesc(productId)
                .stream()
                .map(m -> new StockMovementResponse(
                        m.getQuantityChanged(),
                        m.getType(),
                        m.getReason(),
                        m.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    // ================= PAGINATION =================

    public Page<ProductResponse> getProductsPaged(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCategory() != null
                                ? p.getCategory().getName()
                                : null
                ));
    }
}
