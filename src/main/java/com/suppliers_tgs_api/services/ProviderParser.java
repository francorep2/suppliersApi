package com.suppliers_tgs_api.services;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import java.util.List;

public interface ProviderParser {

     List<ProductDTO> parse(String rawResponse);

    ProviderName supports();
}
