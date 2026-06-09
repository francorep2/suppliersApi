package com.suppliers_tgs_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProductService;
import com.suppliers_tgs_api.services.ProviderService;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import com.suppliers_tgs_api.services.impl.providers.ProviderFactory;
import com.suppliers_tgs_api.services.parser.ProviderParserFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierSearchEngineImpl implements SupplierSearchEngine {

    private final ProviderFactory providerFactory;
    private final ProviderParserFactory parserFactory;
    private final ProductService productService;
    private final ExecutorService providerExecutor;

    private static final long TIMEOUT_MS = 1200;

    @Override
    public List<ProductDTO> searchAll(String query) {

        UUID userId = getAuthenticatedUserId();

        List<CompletableFuture<List<ProductDTO>>> futures = new ArrayList<>();

        for (ProviderName provider : ProviderName.values()) {

            CompletableFuture<List<ProductDTO>> future =
                    CompletableFuture.supplyAsync(() -> {

                        try {
                            ProviderService service =
                                    providerFactory.getProvider(provider);

                            return searchByProviderInternal(service, userId, query);

                        } catch (Exception e) {
                            System.out.println("Provider failed: " + provider + " -> " + e.getMessage());
                            return List.<ProductDTO>of();
                        }

                    }, providerExecutor)
                    .completeOnTimeout(List.of(), TIMEOUT_MS, TimeUnit.MILLISECONDS);

            futures.add(future);
        }

        List<ProductDTO> allResults = new ArrayList<>();

        for (CompletableFuture<List<ProductDTO>> future : futures) {
            try {
                allResults.addAll(future.get());
            } catch (Exception e) {
                System.out.println("Future failed: " + e.getMessage());
            }
        }

        return filterResults(allResults, query);
    }

    @Override
    public List<ProductDTO> searchByProvider(ProviderName providerName, String query) {

        UUID userId = getAuthenticatedUserId();

        ProviderService service =
                providerFactory.getProvider(providerName);

        List<ProductDTO> results =
                searchByProviderInternal(service, userId, query);

        return filterResults(results, query);
    }

    private List<ProductDTO> searchByProviderInternal(
            ProviderService service,
            UUID userId,
            String query
    ) {

        String safeQuery = (query == null) ? "" : query;

        if (service.getProviderName() == ProviderName.INVID) {
            return productService.getProductLocalByName(safeQuery);
        }

        String rawResponse =
                service.getElementByName(userId, safeQuery);

        var parser =
                parserFactory.getParser(service.getProviderName());

        return parser.parse(rawResponse);
    }

    private List<ProductDTO> filterResults(List<ProductDTO> list, String query) {

        if (query == null || query.isBlank()) {
            return list;
        }

        String q = normalize(query);

        return list.stream()
                .filter(p -> {
                    String name = normalize(p.getName());

                    if (name.contains(q)) return true;

                    for (String w : name.split(" ")) {
                        if (w.contains(q)) return true;
                    }

                    return false;
                })
                .toList();
    }

    private String normalize(String text) {

        if (text == null) return "";

        return text.toLowerCase()
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ")
                .replace("\u00A0", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private UUID getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof com.suppliers_tgs_api.auth.security.CustomUserDetails user) {
            return user.getId();
        }

        throw new RuntimeException("Invalid authentication principal");
    }

    public List<ProductDTO> searchFiltered(String query, Map<String, Boolean> providers) {

        UUID userId = getAuthenticatedUserId();

        List<CompletableFuture<List<ProductDTO>>> futures = new ArrayList<>();

        for (ProviderName provider : ProviderName.values()) {

            if (providers != null && !providers.isEmpty()) {
                Boolean enabled = providers.get(provider.name());
                if (!Boolean.TRUE.equals(enabled)) continue;
            }

            CompletableFuture<List<ProductDTO>> future =
                    CompletableFuture.supplyAsync(() -> {

                        try {
                            ProviderService service =
                                    providerFactory.getProvider(provider);

                            return searchByProviderInternal(service, userId, query);

                        } catch (Exception e) {
                            System.out.println("Provider failed: " + provider + " -> " + e.getMessage());
                            return List.<ProductDTO>of();
                        }

                    }, providerExecutor)
                    .completeOnTimeout(List.of(), TIMEOUT_MS, TimeUnit.MILLISECONDS);

            futures.add(future);
        }

        List<ProductDTO> allResults = new ArrayList<>();

        for (CompletableFuture<List<ProductDTO>> future : futures) {
            try {
                allResults.addAll(future.get());
            } catch (Exception e) {
                System.out.println("Future failed: " + e.getMessage());
            }
        }

        return filterResults(allResults, query);
    }
}