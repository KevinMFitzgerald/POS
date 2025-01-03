package ie.atu.pos;

import ie.atu.pos.model.MyAppUser;
import ie.atu.pos.model.MyAppUserRepository;
import ie.atu.pos.repository.InventoryRepo;
import ie.atu.pos.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final MyAppUserRepository myAppUserRepository;
    private final InventoryRepo productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(MyAppUserRepository myAppUserRepository, InventoryRepo productRepository, PasswordEncoder passwordEncoder) {
        this.myAppUserRepository = myAppUserRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        MyAppUser admin = new MyAppUser();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setEmail("admin@example.com");
        admin.setRole("ADMIN");
        MyAppUser user = new MyAppUser();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setEmail("user@example.com");
        user.setRole("USER");

        myAppUserRepository.save(admin);
        myAppUserRepository.save(user);

        // Adding Products
        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setCategory("Electronics");
        product1.setPrice(999.99);
        product1.setQuantity(10);

        Product product2 = new Product();
        product2.setName("Smartphone");
        product2.setCategory("Electronics");
        product2.setPrice(599.99);
        product2.setQuantity(25);

        Product product3 = new Product();
        product3.setName("Desk Chair");
        product3.setCategory("Furniture");
        product3.setPrice(149.99);
        product3.setQuantity(15);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}
