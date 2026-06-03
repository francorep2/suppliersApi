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
public class GrupoNucleoParser implements ProviderParser {

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public List<ProductDTO> parse(String rawResponse) {

        List<ProductDTO> products =
                new ArrayList<>();

        try {

            JsonNode array =
                    objectMapper.readTree(rawResponse);

            if (!array.isArray()) {
                return products;
            }

            for (JsonNode item : array) {

                ProductDTO dto =
                        new ProductDTO();

                dto.setProvider("GRUPO_NUCLEO");

                if (item.has("codigo")) {
                    dto.setExternalId(
                            item.get("codigo").asText()
                    );
                }

                if (item.has("item_desc_0")) {
                    dto.setName(
                            item.get("item_desc_0").asText()
                    );
                }

                if (item.has("precioNeto_USD")) {
                    dto.setPrice(
                            item.get("precioNeto_USD").asText()
                    );
                }

                if (item.has("url_imagenes")
                        && item.get("url_imagenes").isArray()
                        && item.get("url_imagenes").size() > 0) {

                    JsonNode firstImage =
                            item.get("url_imagenes").get(0);

                    if (firstImage.has("url")) {
                        dto.setImageUrl(
                                firstImage.get("url").asText()
                        );
                    }
                }


                products.add(dto);
            }

            return products;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed parsing GRUPO_NUCLEO response",
                    e
            );
        }
    }

    @Override
    public ProviderName supports() {
        return ProviderName.GRUPO_NUCLEO;
    }
}