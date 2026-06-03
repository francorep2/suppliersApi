package com.suppliers_tgs_api.services.impl;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.InvidProduct;
import com.suppliers_tgs_api.repositories.InvidProductRepository;
import com.suppliers_tgs_api.services.ProductService;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final InvidProductRepository repository;

    @Override
    public List<ProductDTO> getProductLocalByName(String name) {
        return repository
                .findByTitleContainingIgnoreCase(name)
                .stream()
                .map(this::toDto)
                .toList();
    }

     private ProductDTO toDto(InvidProduct product) {

        ProductDTO dto = new ProductDTO();

        dto.setProvider("INVID");
        dto.setExternalId(product.getId());
        dto.setName(product.getTitle());
        dto.setPrice(product.getPrice().toString());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }
    
}
