package com.suppliers_tgs_api.dto.response;

import java.util.List;

import com.suppliers_tgs_api.dto.ProductDTO;

import lombok.Data;


@Data
public class InvidProductsResponse {

    private List<ProductDTO> productos;

    private Integer totalPages;
}