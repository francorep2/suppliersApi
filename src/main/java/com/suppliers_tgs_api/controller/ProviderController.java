package com.suppliers_tgs_api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderFacadeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
@Tag(name = "Providers", description = "Operaciones sobre proveedores externos")
public class ProviderController {

    private final ProviderFacadeService facadeService;

    @GetMapping("/{providerName}/search")
    @Operation(summary = "Buscar productos en proveedor externo")
    @ApiResponse(responseCode = "200", description = "Search executed successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public String search(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName,
            @RequestParam String name
    ) {
        return facadeService.search(user.getId(), providerName, name);
    }
}