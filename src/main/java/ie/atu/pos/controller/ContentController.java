package ie.atu.pos.controller;
import ie.atu.pos.model.*;
import ie.atu.pos.repository.InventoryRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ContentController {

    @GetMapping("/req/login")
    public String login() {
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    private final InventoryRepo productRepository;

    public ContentController(InventoryRepo productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/index")
    public String showHomePage(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "index";
    }
}

