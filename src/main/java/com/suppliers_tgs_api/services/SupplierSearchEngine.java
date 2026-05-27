package com.suppliers_tgs_api.services;

import java.util.UUID;
import java.util.List;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.dto.ProductDTO;

public interface SupplierSearchEngine {

        List<ProductDTO> searchAll(UUID userId, String query);

        List<ProductDTO> searchByProvider(UUID userId, ProviderName providerName, String query);


    
}
