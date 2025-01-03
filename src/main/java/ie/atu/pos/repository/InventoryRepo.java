package ie.atu.pos.repository;

import ie.atu.pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByQuantity(int quantity);
}
