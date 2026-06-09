package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.auth.AuthResponseDTO;
import br.com.fiap.orbitalguard.dto.auth.LoginRequestDTO;
import br.com.fiap.orbitalguard.dto.auth.RegisterRequestDTO;
import br.com.fiap.orbitalguard.exception.BusinessException;
import br.com.fiap.orbitalguard.model.Usuario;
import br.com.fiap.orbitalguard.model.enums.Role;
import br.com.fiap.orbitalguard.repository.UsuarioRepository;
import br.com.fiap.orbitalguard.security.CustomUserDetails;
import br.com.fiap.orbitalguard.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email ja cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role(Role.OPERADOR)
                .build();

        usuarioRepository.save(usuario);
        CustomUserDetails userDetails = new CustomUserDetails(usuario);
        return AuthResponseDTO.of(jwtService.generateToken(userDetails));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessException("Usuario nao encontrado"));

        CustomUserDetails userDetails = new CustomUserDetails(usuario);
        return AuthResponseDTO.of(jwtService.generateToken(userDetails));
    }
}
