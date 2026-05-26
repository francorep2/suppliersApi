package com.suppliers_tgs_api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.suppliers_tgs_api.auth.AuthService;
import com.suppliers_tgs_api.dto.LoginRequest;
import com.suppliers_tgs_api.dto.LoginResponse;
import com.suppliers_tgs_api.dto.RegisterRequest;
import com.suppliers_tgs_api.dto.RegisterResponse;
import com.suppliers_tgs_api.response.ApiResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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