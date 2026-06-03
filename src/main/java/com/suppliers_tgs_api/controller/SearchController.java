package com.suppliers_tgs_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import com.suppliers_tgs_api.services.impl.SupplierSearchEngineImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Búsqueda de productos en múltiples proveedores")
public class SearchController {

    private final SupplierSearchEngine searchEngine;
    private final SupplierSearchEngineImpl searchEngineImpl;

    @GetMapping("/all")
    @Operation(summary = "Buscar en todos los proveedores")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public List<ProductDTO> searchAll(
            @RequestParam String name
    ) {
        return searchEngine.searchAll(name);
    }

    @GetMapping("/provider/{provider}")
    @Operation(summary = "Buscar en un proveedor específico")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid provider")
    public List<ProductDTO> searchByProvider(
            @PathVariable ProviderName provider,
            @RequestParam String name
    ) {
        return searchEngine.searchByProvider(provider, name);
    }

    @PostMapping("/{name}")
    @Operation(summary = "Búsqueda filtrada por proveedores")
    @ApiResponse(responseCode = "200", description = "Filtered products retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    public List<ProductDTO> search(
            @PathVariable String name,
            @RequestBody(required = false) Map<String, Boolean> providers
    ) {
        return searchEngineImpl.searchFiltered(name, providers);
    }
}