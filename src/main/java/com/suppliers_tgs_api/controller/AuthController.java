package com.suppliers_tgs_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.request.LoginRequest;
import com.suppliers_tgs_api.dto.request.RegisterRequest;
import com.suppliers_tgs_api.dto.response.ApiResponse;
import com.suppliers_tgs_api.dto.response.LoginResponse;
import com.suppliers_tgs_api.dto.response.RegisterResponse;
import com.suppliers_tgs_api.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {

        RegisterResponse response = authService.register(request);

        return new ApiResponse<>(
                true,
                "User created successfully",
                response
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(request);

        return new ApiResponse<>(
                true,
                "Login successful",
                response
        );
    }
}