package ie.atu.pos.controller;


import ie.atu.pos.model.Product;
import ie.atu.pos.service.InventoryService;
import ie.atu.pos.service.InsufficientStockException;
import ie.atu.pos.service.ProductNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getProduct(@PathVariable Long id) {
        try {
            Product product = inventoryService.getOneProduct(id);
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setQuantity(product.getQuantity());
            return ResponseEntity.ok(productDto);
        } catch (ProductNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
        }
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
