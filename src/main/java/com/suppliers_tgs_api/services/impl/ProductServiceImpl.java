package com.suppliers_tgs_api.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.InvidProduct;
import com.suppliers_tgs_api.repositories.InvidProductRepository;
import com.suppliers_tgs_api.services.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final InvidProductRepository repository;

    @Override
    public List<ProductDTO> getProductLocalByName(String name) {
        UUID userId = ((CustomUserDetails)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                .getId();

        return repository
                .findByUserIdAndTitleContainingIgnoreCase(userId, name)
                .stream()
                .map(this::toDto)
                .toList();
    }

     private ProductDTO toDto(InvidProduct product) {

        ProductDTO dto = new ProductDTO();

        dto.setProvider("INVID");
        dto.setExternalId(product.getInvidProductId());
        dto.setName(product.getTitle());
        dto.setPrice(product.getPrice().toString());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }
    
}
