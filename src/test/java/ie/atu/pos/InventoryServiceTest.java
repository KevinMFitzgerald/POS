package ie.atu.pos;

import ie.atu.pos.model.Product;
import ie.atu.pos.repository.InventoryRepo;
import ie.atu.pos.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "Laptop", "Electronics", 100.0, 5);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(product));

        Product result = inventoryService.getOneProduct(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    public void testDecreaseStockSufficient() {
        Product product = new Product(1L, "Laptop", "Electronics", 1000.0, 5);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(product));

        inventoryService.decrementStock(1L, 3);

        verify(inventoryRepo, times(1)).save(any(Product.class));
        assertEquals(2, product.getQuantity());
    }

    @Test
    public void testDecreaseStockInsufficient() {
        Product product = new Product(1L, "Laptop", "Electronics", 1000.0, 2);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> {
            inventoryService.decrementStock(1L, 3);
        });
    }
}
