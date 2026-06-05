package com.suppliers_tgs_api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.services.ProductService;
import com.suppliers_tgs_api.services.impl.InvidSyncServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sync/invid")
@RequiredArgsConstructor
public class InvidSyncController {

    private final InvidSyncServiceImpl syncService;
    private final ProductService service;

    @GetMapping("/{userId}")
    public String sync(
            @PathVariable UUID userId
    ) {

        syncService.sync(userId);

        return "Sync completed at " + LocalDateTime.now();
    }

    @GetMapping("/search/{title}")
    public List<ProductDTO> search(
            @PathVariable String title
    ) {

        return service.getProductLocalByName(title);
    }
}