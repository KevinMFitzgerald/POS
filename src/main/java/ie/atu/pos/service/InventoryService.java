package ie.atu.pos.service;

import ie.atu.pos.model.Product;
import ie.atu.pos.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    public List<Product> getAllProducts() {
        return inventoryRepo.findAll();
    }

    public Product addProduct(String name, double price, int quantity) {
        return inventoryRepo.save(
                Product.builder()
                        .name(name)
                        .price(price)
                        .quantity(quantity)
                        .build()
        );
    }

    @Transactional
    public Product updateProduct(Long id, Double price, Integer quantity) {
        Product product = inventoryRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        if (price != null) {
            product.setPrice(price);
        }
        if (quantity != null) {
            product.setQuantity(quantity);
        }
        // Because we're @Transactional, changes are saved automatically at method completion
        return product;
    }

    @Transactional
    public void decrementStock(Long productId, int amount) {
        Product product = inventoryRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        if (product.getQuantity() < amount) {
            throw new InsufficientStockException(
                    "Not enough stock (requested " + amount
                            + ", available " + product.getQuantity() + ")"
            );
        }
        product.setQuantity(product.getQuantity() - amount);
        inventoryRepo.save(product);  // save for clarity in tests
    }

    public void deleteProduct(Long productId) {
        if (!inventoryRepo.existsById(productId)) {
            throw new ProductNotFoundException("Cannot delete. Product with ID: " + productId + " not found.");
        }
        inventoryRepo.deleteById(productId);
    }

    public Product getOneProduct(Long id) {
        return inventoryRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with ID: " + id)
                );
    }
}
