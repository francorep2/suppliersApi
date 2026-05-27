package com.suppliers_tgs_api.services.parser;

import org.springframework.stereotype.Component;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderParser;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Component
public class InvidParser implements ProviderParser {

    @Override
    public List<ProductDTO> parse(String rawResponse) {
        ProductDTO dto = new ProductDTO();
        dto.setProvider("INVID");
        dto.setRaw(rawResponse);
        return Arrays.asList(dto);
    }

    @Override
    public ProviderName supports() {
        return ProviderName.INVID;
    }
}