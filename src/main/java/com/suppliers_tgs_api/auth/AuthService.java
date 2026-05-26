package com.suppliers_tgs_api.auth;

import com.suppliers_tgs_api.dto.LoginRequest;
import com.suppliers_tgs_api.dto.LoginResponse;
import com.suppliers_tgs_api.dto.RegisterRequest;
import com.suppliers_tgs_api.dto.RegisterResponse;
import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.suppliers_tgs_api.auth.security.JwtService;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .startDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername()
        );
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token);
    }
}