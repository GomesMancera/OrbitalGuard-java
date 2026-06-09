package br.com.fiap.orbitalguard.config;

import br.com.fiap.orbitalguard.model.Usuario;
import br.com.fiap.orbitalguard.model.enums.Role;
import br.com.fiap.orbitalguard.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedAdminUser() {
        return args -> {
            if (!usuarioRepository.existsByEmail("admin@orbitalguard.com")) {
                usuarioRepository.save(Usuario.builder()
                        .nome("Administrador")
                        .email("admin@orbitalguard.com")
                        .senha(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}
