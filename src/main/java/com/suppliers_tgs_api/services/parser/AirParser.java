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
public class AirParser implements ProviderParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProductDTO> parse(String rawResponse) {

        List<ProductDTO> products = new ArrayList<>();

        try {
            if (rawResponse == null || rawResponse.isBlank()) {
                return products;
            }

            JsonNode root = objectMapper.readTree(rawResponse);

            // AIR can return either an array or an object wrapping results
            JsonNode arrayNode = root;

            if (!root.isArray() && root.has("results")) {
                arrayNode = root.get("results");
            }

            if (!arrayNode.isArray()) {
                return products;
            }

            for (JsonNode item : arrayNode) {

                ProductDTO dto = new ProductDTO();

                dto.setProvider("AIR");
                dto.setRaw(item.toString());

                // ID / externalId
                if (item.has("id")) {
                    dto.setExternalId(item.get("id").asText());
                } else if (item.has("code")) {
                    dto.setExternalId(item.get("code").asText());
                } else if (item.has("sku")) {
                    dto.setExternalId(item.get("sku").asText());
                }

                // NAME
                if (item.has("name")) {
                    dto.setName(item.get("name").asText());
                } else if (item.has("title")) {
                    dto.setName(item.get("title").asText());
                } else if (item.has("description")) {
                    dto.setName(item.get("description").asText());
                }

                // PRICE
                if (item.has("price")) {
                    dto.setPrice(item.get("price").asText());
                } else if (item.has("amount")) {
                    dto.setPrice(item.get("amount").asText());
                }

                // IMAGE
                if (item.has("image_url")) {
                    dto.setImageUrl(item.get("image_url").asText());
                } else if (item.has("imageUrl")) {
                    dto.setImageUrl(item.get("imageUrl").asText());
                }

                // only add if name exists (important for filtering logic)
                if (dto.getName() != null && !dto.getName().isBlank()) {
                    products.add(dto);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed parsing AIR response", e);
        }

        return products;
    }

    @Override
    public ProviderName supports() {
        return ProviderName.AIR;
    }
}