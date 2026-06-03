package com.suppliers_tgs_api.services;

import java.util.List;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.dto.ProductDTO;

public interface SupplierSearchEngine {

        List<ProductDTO> searchAll(String query);

        List<ProductDTO> searchByProvider(ProviderName providerName, String query);


    
}
