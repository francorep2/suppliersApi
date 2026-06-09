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

            String cleanedResponse = rawResponse
                    .replaceAll("<br\\s*/?>", "")
                    .replaceAll("<b>.*?</b>", "")
                    .replaceAll("<[^>]*>", "")
                    .trim();

            int start = cleanedResponse.indexOf("[");
            int end = cleanedResponse.lastIndexOf("]");

            if (start == -1 || end == -1 || end <= start) {
                throw new RuntimeException("AIR: invalid JSON response after cleaning");
            }

            String json = cleanedResponse.substring(start, end + 1);

            JsonNode root = objectMapper.readTree(json);

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

                dto.setExternalId(item.path("codigo").asText(null));
                dto.setName(item.path("descrip").asText(null));
                dto.setImageUrl("No Air URL Image");

                long price = item.path("precio").asLong(0);

                long tax = item.path("impuesto_iva")
                        .path("alicuota")
                        .asLong(0);

                long internalTax = item.path("impuesto_interno")
                        .path("alicuota")
                        .asLong(0);

                double finalPrice = price
                        * (1 + tax / 100.0)
                        * (1 + internalTax / 100.0);

                dto.setPrice(String.valueOf((long) finalPrice));

                if (item.path("ros").path("disponible").asInt(0) > 0) {
                    dto.setLocationAir(List.of("Rosario"));

                } else if (item.path("mza").path("disponible").asInt(0) > 0) {
                    dto.setLocationAir(List.of("Mendoza"));

                } else if (item.path("cba").path("disponible").asInt(0) > 0) {
                    dto.setLocationAir(List.of("Cordoba"));

                } else if (item.path("caba").path("disponible").asInt(0) > 0) {
                    dto.setLocationAir(List.of("CABA (Lugano)"));
                }

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
