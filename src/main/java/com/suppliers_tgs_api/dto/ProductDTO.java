package com.suppliers_tgs_api.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String provider;
    private String name;
    private String price;
    private String imageUrl;
    private String externalId;
    private String raw; // opcional: JSON crudo del proveedor
} 
    

