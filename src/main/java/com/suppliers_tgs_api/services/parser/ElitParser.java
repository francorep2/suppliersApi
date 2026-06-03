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
public class ElitParser implements ProviderParser {

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

            if (!root.isArray()) {

                if (root.has("resultado") && root.get("resultado").isArray()) {
                    arrayNode = root.get("resultado");

                } else if (root.has("results") && root.get("results").isArray()) {
                    arrayNode = root.get("results");
                }
            }

            if (!arrayNode.isArray()) {
                return products;
            }

            for (JsonNode item : arrayNode) {

                ProductDTO dto = new ProductDTO();

                dto.setProvider("ELIT");

                // externalId (ELIT schema)
                if (item.has("id")) {
                    dto.setExternalId(item.get("id").asText());
                } else if (item.has("codigo_producto")) {
                    dto.setExternalId(item.get("codigo_producto").asText());
                } else if (item.has("codigo_alfa")) {
                    dto.setExternalId(item.get("codigo_alfa").asText());
                }

                // name (IMPORTANT: ELIT uses 'nombre')
                if (item.has("nombre")) {
                    dto.setName(item.get("nombre").asText());
                } else if (item.has("name")) {
                    dto.setName(item.get("name").asText());
                } else if (item.has("title")) {
                    dto.setName(item.get("title").asText());
                }

                // price
                if (item.has("pvp_usd")) {
                    dto.setPrice(item.get("pvp_usd").asText());
                } else if (item.has("precio")) {
                    dto.setPrice(item.get("precio").asText());
                } else if (item.has("price")) {
                    dto.setPrice(item.get("price").asText());
                }

                // imageUrl (ELIT: imagenes array of strings)
                if (item.has("imagenes") && item.get("imagenes").isArray() && item.get("imagenes").size() > 0) {
                    dto.setImageUrl(item.get("imagenes").get(0).asText());
                } else if (item.has("image_url")) {
                    dto.setImageUrl(item.get("image_url").asText());
                }

                // IMPORTANT: NO FILTERING - always add
                products.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed parsing ELIT response", e);
        }

        return products;
    }

    @Override
    public ProviderName supports() {
        return ProviderName.ELIT;
    }
}