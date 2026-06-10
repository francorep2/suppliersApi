package com.suppliers_tgs_api.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {

    private String provider;
    private String name;
    private String price;
    private String imageUrl;
    private String externalId;
    private List<String> locationAir;
    private String sku;
    private String iva;
    private String impInterno;
    private String brand;
    private String category;
} 
    

