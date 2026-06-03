package com.suppliers_tgs_api.controller;

import java.util.List;  

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.suppliers_tgs_api.dto.request.ProviderSearchRequest;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import com.suppliers_tgs_api.services.impl.SupplierSearchEngineImpl;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SupplierSearchEngine searchEngine;
    private final SupplierSearchEngineImpl searchEngineImpl;

    @GetMapping("/all")
    public List<ProductDTO> searchAll(
            @RequestParam String name
    ) {
        return searchEngine.searchAll(name);
    }

    @GetMapping("/provider/{provider}")
    public List<ProductDTO> searchByProvider(
            @PathVariable ProviderName provider,
            @RequestParam String name
    ) {
        return searchEngine.searchByProvider(provider, name);
    }

        @PostMapping("/{name}")
    public List<ProductDTO> search(
            @PathVariable String name,
            @RequestBody(required = false) Map<String, Boolean> providers
    ) {
        return searchEngineImpl.searchFiltered(name, providers);
    }
}