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

            JsonNode array = root;

            if (!root.isArray() && root.has("data")) {
                array = root.get("data");
            } else if (!root.isArray() && root.has("results")) {
                array = root.get("results");
            }

            if (!array.isArray()) {
                return products;
            }

            for (JsonNode item : array) {

                ProductDTO dto = new ProductDTO();

                dto.setProvider("INVID");

                // externalId
                if (item.has("ID")) {
                    dto.setExternalId(item.get("ID").asText());
                } else if (item.has("id")) {
                    dto.setExternalId(item.get("id").asText());
                } else if (item.has("code")) {
                    dto.setExternalId(item.get("code").asText());
                } else if (item.has("sku")) {
                    dto.setExternalId(item.get("sku").asText());
                }

                // NAME SIMPLE (MISMA FILOSOFÍA QUE GRUPO_NUCLEO)
                if (item.has("TITLE")) {
                    dto.setName(item.get("TITLE").asText());
                } else if (item.has("item_desc_0")) {
                    dto.setName(item.get("item_desc_0").asText());
                } else if (item.has("item_desc_1")) {
                    dto.setName(item.get("item_desc_1").asText());
                } else if (item.has("DESCRIPTION")) {
                    dto.setName(item.get("DESCRIPTION").asText());
                }

                // PRICE SIMPLE
                if (item.has("PRICE")) {
                    dto.setPrice(item.get("PRICE").asText());
                } else if (item.has("FINAL_PRICE")) {
                    dto.setPrice(item.get("FINAL_PRICE").asText());
                } else if (item.has("price")) {
                    dto.setPrice(item.get("price").asText());
                }

                // IMAGE SIMPLE
                if (item.has("IMAGE_URL")) {
                    dto.setImageUrl(item.get("IMAGE_URL").asText());
                } else if (item.has("image_url")) {
                    dto.setImageUrl(item.get("image_url").asText());
                } else if (item.has("url_imagenes")
                        && item.get("url_imagenes").isArray()
                        && item.get("url_imagenes").size() > 0
                        && item.get("url_imagenes").get(0).has("url")) {

                    dto.setImageUrl(
                            item.get("url_imagenes")
                                    .get(0)
                                    .get("url")
                                    .asText()
                    );
                }

                dto.setRaw(item.toString());

                // SIEMPRE agregar (igual que GRUPO_NUCLEO)
                products.add(dto);
            }

            // DEBUG MODE: return ONLY ONE item to inspect final DTO schema
            if (products.isEmpty()) {
                System.out.println("[INVID DEBUG] no products parsed");
                return products;
            }

            ProductDTO first = products.get(0);

            List<ProductDTO> singleResult = new ArrayList<>();
            singleResult.add(first);

            try {
                System.out.println("[INVID DEBUG] SINGLE OUTPUT DTO = " + objectMapper.writeValueAsString(first));
            } catch (Exception e) {
                System.out.println("[INVID DEBUG] could not serialize DTO");
            }

            return singleResult;

        } catch (Exception e) {
            throw new RuntimeException("Failed parsing INVID response", e);
        }
    }

    @Override
    public ProviderName supports() {
        return ProviderName.INVID;
    }
}