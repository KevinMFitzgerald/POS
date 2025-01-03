package ie.atu.pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN")
                .build();

        UserDetails buyer = User.builder()
                .username("buyer")
                .password(passwordEncoder.encode("buyerpass"))
                .roles("BUYER")
                .build();

        return new InMemoryUserDetailsManager(admin, buyer);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *      Configure your SecurityFilterChain:
     *    - Require certain roles for specific HTTP methods/paths
     *    - Use formLogin for quick testing (auto-generated login page)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // Let ADMIN or BUYER view products (GET)
                        .requestMatchers(HttpMethod.GET, "/api/inventory/products/**").hasAnyRole("ADMIN", "BUYER")

                        // Only ADMIN can create (POST), update (PUT), delete (DELETE) products
                        // will need to update to only allow whats needed for admin. ie product price change, add money to user etc
                        .requestMatchers(HttpMethod.POST, "/api/inventory/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/inventory/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/inventory/products/**").hasRole("ADMIN")

                        // Everything else must be authenticated
                        .anyRequest().authenticated()
                )
                // Enable form-based login (Spring Boot's default login form)
                .formLogin(Customizer.withDefaults())
                .build();
        //need html to go to otherwise white labbled page
    }
}
