package com.suppliers_tgs_api.services;

import com.suppliers_tgs_api.dto.ProductDTO;
import java.util.List;

public interface ProductService {

     public List<ProductDTO> getProductLocalByName(String name);

    
}
