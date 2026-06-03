package com.suppliers_tgs_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.request.UserRequest;
import com.suppliers_tgs_api.dto.response.ApisResponse;
import com.suppliers_tgs_api.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Administración de usuarios (solo ADMIN)")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-end-date")
    @Operation(summary = "Actualizar fecha de expiración del usuario")
    @ApiResponse(responseCode = "200", description = "User end date updated successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN only")
    public ApisResponse<Void> updateEndDateForUser(@RequestBody UserRequest request) {

        userService.updateEndDateForUser(request.getUserId());

        return new ApisResponse<>(
                true,
                "User end date updated successfully",
                null
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-active-status")
    @Operation(summary = "Actualizar estado activo del usuario")
    @ApiResponse(responseCode = "200", description = "User active status updated successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN only")
    public ApisResponse<Void> updateUserActiveStatus(@RequestBody UserRequest request) {

        userService.updateUserActiveStatus(
                request.getUserId(),
                request.getActive()
        );

        return new ApisResponse<>(
                true,
                "User active status updated successfully",
                null
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar usuario")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN only")
    public ApisResponse<Void> deleteUser(@RequestBody UserRequest request) {

        userService.deleteUser(request.getUserId());

        return new ApisResponse<>(
                true,
                "User deleted successfully",
                null
        );
    }
}