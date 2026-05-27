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
public class NewBytesParser implements ProviderParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProductDTO> parse(String rawResponse) {

        List<ProductDTO> products = new ArrayList<>();

        try {
            if (rawResponse == null || rawResponse.isBlank()) {
                return products;
            }

            JsonNode root = objectMapper.readTree(rawResponse);

            JsonNode arrayNode = root;

            // NEW_BYTES may return array directly or wrapped response
            if (!root.isArray() && root.has("data")) {
                arrayNode = root.get("data");
            } else if (!root.isArray() && root.has("results")) {
                arrayNode = root.get("results");
            } else if (!root.isArray() && root.has("items")) {
                arrayNode = root.get("items");
            }

            if (!arrayNode.isArray()) {
                return products;
            }

            for (JsonNode item : arrayNode) {

                ProductDTO dto = new ProductDTO();

                dto.setProvider("NEW_BYTES");
                dto.setRaw(item.toString());

                // externalId
                if (item.has("id")) {
                    dto.setExternalId(item.get("id").asText());
                } else if (item.has("code")) {
                    dto.setExternalId(item.get("code").asText());
                } else if (item.has("sku")) {
                    dto.setExternalId(item.get("sku").asText());
                }

                // name
                if (item.has("name")) {
                    dto.setName(item.get("name").asText());
                } else if (item.has("title")) {
                    dto.setName(item.get("title").asText());
                } else if (item.has("description")) {
                    dto.setName(item.get("description").asText());
                }

                // price
                if (item.has("price")) {
                    dto.setPrice(item.get("price").asText());
                } else if (item.has("amount")) {
                    dto.setPrice(item.get("amount").asText());
                }

                // image
                if (item.has("image_url") && !item.get("image_url").isNull()) {
                    dto.setImageUrl(item.get("image_url").asText());
                } else if (item.has("imageUrl") && !item.get("imageUrl").isNull()) {
                    dto.setImageUrl(item.get("imageUrl").asText());
                } else if (item.has("mainImage") && !item.get("mainImage").isNull()) {
                    // NEW_BYTES primary image
                    dto.setImageUrl(item.get("mainImage").asText());
                } else if (item.has("mainImageExp") && !item.get("mainImageExp").isNull()) {
                    // fallback image
                    dto.setImageUrl(item.get("mainImageExp").asText());
                }

                // only valid products (strict rule)
                if (dto.getName() != null && !dto.getName().isBlank()) {
                    products.add(dto);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed parsing NEW_BYTES response", e);
        }

        return products;
    }

    @Override
    public ProviderName supports() {
        return ProviderName.NEW_BYTES;
    }
}