package ie.atu.pos.controller;


import ie.atu.pos.model.Product;
import ie.atu.pos.service.InventoryService;
import ie.atu.pos.service.InsufficientStockException;
import ie.atu.pos.service.ProductNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping("/products")
    public List<Product> listAll() {
        return inventoryService.getAllProducts();
    }
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        Product created = inventoryService.addProduct(
                request.getName(),
                request.getPrice(),
                request.getQuantity()
        );
        return ResponseEntity.ok(created);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody ProductRequest request) {
        try {
            Product updated = inventoryService.updateProduct(id, request.getPrice(), request.getQuantity());
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/products/{id}/decrement")
    public ResponseEntity<String> decrementStock(@PathVariable Long id,
                                                 @RequestBody DecrementRequest request) {
        try {
            inventoryService.decrementStock(id, request.getAmount());
            return ResponseEntity.ok("Stock decremented by " + request.getAmount());
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientStockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            inventoryService.deleteProduct(id);
            return ResponseEntity.ok("Product with ID " + id + " deleted.");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    public static class ProductRequest {
        private String name;
        private Double price;
        private Integer quantity;
    }

    @Data
    public static class DecrementRequest {
        private int amount;
    }
}
