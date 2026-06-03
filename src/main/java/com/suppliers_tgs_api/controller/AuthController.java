package com.suppliers_tgs_api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.request.LoginRequest;
import com.suppliers_tgs_api.dto.request.RegisterRequest;
import com.suppliers_tgs_api.dto.response.ApisResponse;
import com.suppliers_tgs_api.dto.response.LoginResponse;
import com.suppliers_tgs_api.dto.response.RegisterResponse;
import com.suppliers_tgs_api.services.AuthService;
import com.suppliers_tgs_api.services.impl.InvidSyncServiceImpl;
import com.suppliers_tgs_api.utils.JwtDecoderUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticación y registro de usuarios")
public class AuthController {

    private final AuthService authService;
    private final InvidSyncServiceImpl invidsync;
    private final JwtDecoderUtil decoder;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    @Operation(summary = "Register user")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public ApisResponse<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {

        RegisterResponse response = authService.register(request);

        return new ApisResponse<>(
                true,
                "User created successfully",
                response
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ApisResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(request);

        // Esto sincroniza invid cada login pero hace que demore 1min
        // invidsync.sync(decoder.getUserId(response.getToken()));

        return new ApisResponse<>(
                true,
                "Login successful",
                response
        );
    }
}