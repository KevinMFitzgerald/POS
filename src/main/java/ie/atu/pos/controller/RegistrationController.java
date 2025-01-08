package ie.atu.pos.controller;

import ie.atu.pos.model.MyAppUser;
import ie.atu.pos.model.MyAppUserRepository;
import ie.atu.pos.client.PaymentClient;
import ie.atu.pos.client.PaymentClient.AllocateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
public class RegistrationController {

    private final MyAppUserRepository myAppUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentClient paymentClient;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public MyAppUser createUser(@RequestBody MyAppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getUsername()+"@gmail.com");
        MyAppUser savedUser = myAppUserRepository.save(user);
        Random random = new Random();
        int randomNumber = 5000 + random.nextInt(100000 - 5000 + 1);
        AllocateRequest allocateRequest = new AllocateRequest();
        allocateRequest.setBuyerUsername(savedUser.getUsername());
        allocateRequest.setAmount(randomNumber);

        try {
            paymentClient.allocate(allocateRequest);  // Feign Client call to PaymentService
        } catch (Exception e) {
            System.err.println("Failed to allocate funds: " + e.getMessage());
        }

        return savedUser;
    }
}