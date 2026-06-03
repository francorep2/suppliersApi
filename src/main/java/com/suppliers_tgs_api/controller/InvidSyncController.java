package com.suppliers_tgs_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.services.ProductService;
import com.suppliers_tgs_api.services.impl.InvidSyncServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sync/invid")
@RequiredArgsConstructor
@Tag(name = "Invid Sync", description = "Sincronización y búsqueda de productos Invid")
public class InvidSyncController {

    private final InvidSyncServiceImpl syncService;
    private final ProductService service;

    @GetMapping("/{userId}")
    @Operation(summary = "Ejecutar sincronización manual de Invid")
    @ApiResponse(responseCode = "200", description = "Sync completed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid userId")
    public String sync(
            @PathVariable UUID userId
    ) {

        syncService.sync(userId);

        return "Sync completed at " + System.currentTimeMillis();
    }

    @GetMapping("/search/{title}")
    @Operation(summary = "Buscar productos locales por nombre")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public List<ProductDTO> search(
            @PathVariable String title
    ) {

        return service.getProductLocalByName(title);
    }
}