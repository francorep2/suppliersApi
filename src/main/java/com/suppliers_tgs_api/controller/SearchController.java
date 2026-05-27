package com.suppliers_tgs_api.controller;

import java.util.List;
import java.util.UUID;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SupplierSearchEngine searchEngine;

    @GetMapping("/all")
    public List<ProductDTO> searchAll(
            @RequestParam UUID userId,
            @RequestParam String name
    ) {
        return searchEngine.searchAll(userId, name);
    }

    @GetMapping("/provider/{provider}")
    public List<ProductDTO> searchByProvider(
            @RequestParam UUID userId,
            @PathVariable ProviderName provider,
            @RequestParam String name
    ) {
        return searchEngine.searchByProvider(userId, provider, name);
    }
}