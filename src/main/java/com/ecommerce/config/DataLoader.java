package com.ecommerce.config;

import com.ecommerce.auth.User;
import com.ecommerce.auth.Role;
import com.ecommerce.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ===============================
        // ADMIN DE TESTE
        // ===============================

        var adminOpt = userRepository.findByEmail("admin@teste.com");

        if (adminOpt.isEmpty()) {

            User user = new User();
            user.setName("Admin Teste");
            user.setEmail("admin@teste.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(Role.ADMIN);

            userRepository.save(user);
        } else if (adminOpt.get().getRole() != Role.ADMIN) {
            // autocorre admin antigo criado com role USER
            User admin = adminOpt.get();
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Role do admin corrigida para ADMIN");
        }
        // ===============================
        // CLIENTE DE TESTE
        // ===============================
        if (userRepository.findByEmail("cliente@teste.com").isEmpty()) {

            User cliente = new User();
            cliente.setName("Cliente Teste");
            cliente.setEmail("cliente@teste.com");
            cliente.setPassword(passwordEncoder.encode("123456"));
            cliente.setRole(Role.USER);

            userRepository.save(cliente);
        }
    }
    }

