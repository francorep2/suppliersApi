package com.suppliers_tgs_api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.auth.security.SecurityConfig;
import com.suppliers_tgs_api.model.InvidProduct;
import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.repositories.InvidProductRepository;
import com.suppliers_tgs_api.services.impl.providers.InvidProviderService;
import com.suppliers_tgs_api.utils.JwtDecoderUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvidSyncServiceImpl {

    private final InvidProviderService invidProviderService;
    private final InvidProductRepository repository;
    private final JwtDecoderUtil jwtDecoderUtil;
    private final SecurityConfig securityConfig;

   public void sync(UUID userId) {

    User user = new User();
    user.setId(userId);
    
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

                batch.add(mapProduct(node, user));
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
 public void scheduleSync() {

        UUID userId = ((CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal())
        .getId();
        
        System.out.println("Starting scheduled INVID sync at: " + LocalDateTime.now());

    User user = new User();
    user.setId(userId);

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

                batch.add(mapProduct(node, user));
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

    List<InvidProduct> productsToSave = new ArrayList<>();

    for (InvidProduct product : batch) {

        var existing = repository.findByUserIdAndInvidProductId(
                product.getUser().getId(),
                product.getInvidProductId());

        if (existing.isPresent()) {
            InvidProduct current = existing.get();

            current.setTitle(product.getTitle());
            current.setPartNumber(product.getPartNumber());
            current.setDescription(product.getDescription());
            current.setPrice(product.getPrice());
            current.setStockStatus(product.getStockStatus());
            current.setImageUrl(product.getImageUrl());
            current.setLastSync(product.getLastSync());

            productsToSave.add(current);
        } else {
            productsToSave.add(product);
        }
    }

    repository.saveAll(productsToSave);
}


private InvidProduct mapProduct(JsonNode node, User user) {

    InvidProduct product = new InvidProduct();

    product.setUser(user);

    product.setInvidProductId(
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