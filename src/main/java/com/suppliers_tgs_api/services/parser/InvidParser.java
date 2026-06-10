package com.suppliers_tgs_api.services.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderParser;

@Component
public class InvidParser implements ProviderParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProductDTO> parse(String rawResponse) {

        List<ProductDTO> products = new ArrayList<>();

        try {

            if (rawResponse == null || rawResponse.isBlank()) {
                return products;
            }

            JsonNode root = objectMapper.readTree(rawResponse);

            JsonNode data = root.path("data");

            if (!data.isArray()) {
                return products;
            }

            for (JsonNode item : data) {

                ProductDTO dto = new ProductDTO();

                dto.setProvider("INVID");

                dto.setExternalId(
                        item.path("ID").asText()
                );

                dto.setName(
                        item.path("TITLE").asText()
                );

                dto.setPrice(
                        item.path("PRICE").asText("0")
                );

                dto.setImageUrl(
                        item.path("IMAGE_URL").asText()
                );

                dto.setCategory(item.path("CATEGORY").asText());
                dto.setBrand(item.path("BRAND").asText());
                dto.setSku(item.path("PART_NUMBER").asText());
                dto.setIva(item.path("IVA_PERCENT").asText());
                dto.setImpInterno(item.path("INTERNAL_TAX_PERCENT").asText());


                products.add(dto);
            }

            return products;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed parsing INVID response",
                    e
            );
        }
    }

    @Override
    public ProviderName supports() {
        return ProviderName.INVID;
    }
}