package com.treetoplodge.treetoplodge_api.config;

import com.treetoplodge.treetoplodge_api.model.Role;
import com.treetoplodge.treetoplodge_api.model.User;
import com.treetoplodge.treetoplodge_api.repository.RoleRepository;
import com.treetoplodge.treetoplodge_api.repository.UserRepository;
import com.treetoplodge.treetoplodge_api.util.Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Starting data initialization...");
            initRoles();
            initAdminUser();
            initTestUsers();
            reEncodeExistingPasswords();
            log.info("Data initialization complete");
        };
    }

    private void initRoles() {
        log.info("Initializing roles...");

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(0, Role.ERole.ROLE_CUSTOMER));
            roleRepository.save(new Role(0, Role.ERole.ROLE_SHOP));
            roleRepository.save(new Role(0, Role.ERole.ROLE_ADMIN));
            log.info("Roles created successfully");
        } else {
            log.info("Roles already exist, skipping creation");
        }
    }

    private void initAdminUser() {
        log.info("Checking for admin user...");

        if (!userRepository.existsByUsername("admin")) {
            log.info("Creating admin user...");

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@treetoplodge.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setPhoneNumber("1234567890");
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setCreatedBy("system");
            admin.setUserId(Generator.generateUserId());

            // Assign admin role
            Set<Role> adminRoles = new HashSet<>();
            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            log.info("Admin user created successfully");
        } else {
            log.info("Admin user already exists, skipping creation");
        }
    }

    private void initTestUsers() {
        // Create test customer
        if (!userRepository.existsByUsername("customer")) {
            log.info("Creating test customer user...");

            User customerUser = new User();
            customerUser.setUsername("customer");
            customerUser.setEmail("customer@example.com");
            customerUser.setPassword(passwordEncoder.encode("customer123"));
            customerUser.setFullName("Test Customer");
            customerUser.setPhoneNumber("1234567890");
            customerUser.setActive(true);
            customerUser.setCreatedAt(LocalDateTime.now());
            customerUser.setCreatedBy("system");
            customerUser.setUserId(Generator.generateUserId());

            // Assign customer role
            Set<Role> customerRoles = new HashSet<>();
            Role customerRole = roleRepository.findByName(Role.ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Customer role not found"));
            customerRoles.add(customerRole);
            customerUser.setRoles(customerRoles);

            userRepository.save(customerUser);
            log.info("Test customer user created successfully");
        }

        // Create test shop owner
        if (!userRepository.existsByUsername("shop")) {
            log.info("Creating test shop user...");

            User shopUser = new User();
            shopUser.setUsername("shop");
            shopUser.setEmail("shop@example.com");
            shopUser.setPassword(passwordEncoder.encode("shop123"));
            shopUser.setFullName("Test Shop Owner");
            shopUser.setPhoneNumber("0987654321");
            shopUser.setActive(true);
            shopUser.setCreatedAt(LocalDateTime.now());
            shopUser.setCreatedBy("system");
            shopUser.setUserId(Generator.generateUserId());

            // Assign shop role
            Set<Role> shopRoles = new HashSet<>();
            Role shopRole = roleRepository.findByName(Role.ERole.ROLE_SHOP)
                    .orElseThrow(() -> new RuntimeException("Shop role not found"));
            shopRoles.add(shopRole);
            shopUser.setRoles(shopRoles);

            userRepository.save(shopUser);
            log.info("Test shop user created successfully");
        }
    }

    private void reEncodeExistingPasswords() {
        log.info("Checking for users with unencoded passwords...");

        userRepository.findAll().forEach(user -> {
            // Check if password is not BCrypt encoded
            String password = user.getPassword();
            if (password != null && !password.startsWith("$2a$")) {
                log.info("Re-encoding password for user: {}", user.getUsername());
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                log.info("Password re-encoded for user: {}", user.getUsername());
            }
        });
    }
}