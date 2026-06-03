package com.suppliers_tgs_api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.model.InvidProduct;
import com.suppliers_tgs_api.repositories.InvidProductRepository;
import com.suppliers_tgs_api.services.impl.providers.InvidProviderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvidSyncServiceImpl {

    private final InvidProviderService invidProviderService;
    private final InvidProductRepository repository;
    private final ObjectMapper objectMapper;

   public void sync(UUID userId) {

    String url =
            "https://www.invidcomputers.com/api/v1/articulo.php";

    while (url != null) {

        JsonNode response =
                invidProviderService.callEndpoint(
                        userId,
                        url
                );

        JsonNode products =
                response.path("data");

        List<InvidProduct> batch =
                new ArrayList<>();

        for (JsonNode node : products) {

                if (!isValidProduct(node)) {
                        continue;
                }

                batch.add(mapProduct(node));
                }

        saveOrUpdate(batch);

        JsonNode next =
                response.get("next_page_url");

        String nextUrl =
        next == null || next.isNull()
                ? null
                : next.asText();

        url = (nextUrl == null || nextUrl.isBlank())
        ? null
        : nextUrl;

    }
   }

@Scheduled(cron = "0 */30 * * * *")
 public void scheduleSync(UUID userId) {

        System.out.println("Starting scheduled INVID sync at: " + LocalDateTime.now());

    String url =
            "https://www.invidcomputers.com/api/v1/articulo.php";

    while (url != null) {

        JsonNode response =
                invidProviderService.callEndpoint(
                        userId,
                        url
                );

        JsonNode products =
                response.path("data");

        List<InvidProduct> batch =
                new ArrayList<>();

        for (JsonNode node : products) {

                if (!isValidProduct(node)) {
                        continue;
                }

                batch.add(mapProduct(node));
                }

        saveOrUpdate(batch);

        JsonNode next =
                response.get("next_page_url");

        String nextUrl =
        next == null || next.isNull()
                ? null
                : next.asText();

        url = (nextUrl == null || nextUrl.isBlank())
        ? null
        : nextUrl;

    }
   }

private void saveOrUpdate(List<InvidProduct> batch) {

    if (batch == null || batch.isEmpty()) {
        return;
    }

    repository.saveAll(batch);
}


private InvidProduct mapProduct(JsonNode node) {

    InvidProduct product = new InvidProduct();

    product.setId(
            node.path("ID").asText()
    );

    product.setTitle(
            node.path("TITLE").asText()
    );

    product.setPartNumber(
            node.path("PART_NUMBER").asText()
    );

    product.setDescription(
            node.path("DESCRIPTION").asText()
    );

    String price =
            node.path("PRICE").asText();

    product.setPrice(
            price == null || price.isBlank()
                    ? BigDecimal.ZERO
                    : new BigDecimal(price)
    );

    product.setStockStatus(
            node.path("STOCK_STATUS").asText()
    );

    product.setImageUrl(
            node.path("IMAGE_URL").asText()
    );

    product.setLastSync(
            LocalDateTime.now()
    );
    

    return product;
}

private boolean isValidProduct(JsonNode node) {

    String stock = node.path("STOCK_STATUS").asText("");

    BigDecimal price;

    try {
        price = new BigDecimal(node.path("PRICE").asText("0"));
    } catch (Exception e) {
        return false;
    }

    boolean hasStock =
            stock != null &&
            !stock.trim().equalsIgnoreCase("SIN STOCK") &&
            !stock.trim().equalsIgnoreCase("SIN_STOCK") &&
            !stock.toLowerCase().contains("sin stock") &&
            !stock.toLowerCase().contains("agotado");
    boolean validPrice =
            price.compareTo(BigDecimal.ZERO) > 0;

    return hasStock && validPrice;
}

}