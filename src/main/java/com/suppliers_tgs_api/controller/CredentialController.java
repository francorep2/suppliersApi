package com.suppliers_tgs_api.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.dto.request.CredentialRequest;
import com.suppliers_tgs_api.dto.response.CredentialResponse;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.CredentialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/credentials")
@RequiredArgsConstructor
@Tag(name = "Credentials", description = "Gestión de credenciales por proveedor")
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping
    @Operation(summary = "Crear o actualizar credencial")
    @ApiResponse(responseCode = "200", description = "Credential saved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public CredentialResponse saveOrUpdate(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CredentialRequest request
    ) {
        return credentialService.saveOrUpdate(user.getId(), request);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener credenciales del usuario logueado")
    @ApiResponse(responseCode = "200", description = "Credentials retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public List<CredentialResponse> getMyCredentials(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return credentialService.getAllByUser(user.getId());
    }

    @GetMapping("/{providerName}")
    @Operation(summary = "Obtener credencial por proveedor")
    @ApiResponse(responseCode = "200", description = "Credential found")
    @ApiResponse(responseCode = "404", description = "Credential not found")
    public CredentialResponse getByProvider(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName
    ) {
        return credentialService.getByUserAndProvider(user.getId(), providerName);
    }

    @DeleteMapping("/{providerName}")
    @Operation(summary = "Eliminar credencial por proveedor")
    @ApiResponse(responseCode = "200", description = "Credential deleted successfully")
    @ApiResponse(responseCode = "404", description = "Credential not found")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName
    ) {
        credentialService.delete(user.getId(), providerName);
    }
}