package com.suppliers_tgs_api.controller;

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
import com.suppliers_tgs_api.services.impl.InvidSyncServiceImpl;
import com.suppliers_tgs_api.utils.JwtDecoderUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final InvidSyncServiceImpl invidsync;
    private final JwtDecoderUtil decoder;

        //@PreAuthorize("hasRole('ADMIN')")
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
        invidsync.sync(decoder.getUserId(response.getToken())); 
        

        return new ApiResponse<>(
                true,
                "Login successful",
                response
        );
    }
}