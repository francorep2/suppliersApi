package com.suppliers_tgs_api.services.impl;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.auth.security.JwtService;
import com.suppliers_tgs_api.dto.request.LoginRequest;
import com.suppliers_tgs_api.dto.request.RegisterRequest;
import com.suppliers_tgs_api.dto.response.LoginResponse;
import com.suppliers_tgs_api.dto.response.RegisterResponse;
import com.suppliers_tgs_api.model.Role;
import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.repositories.UserRepository;
import com.suppliers_tgs_api.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    @Override
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

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());

        return new LoginResponse(token);
    }
}