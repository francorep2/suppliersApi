package com.suppliers_tgs_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProductService;
import com.suppliers_tgs_api.services.ProviderService;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import com.suppliers_tgs_api.services.impl.providers.ProviderFactory;
import com.suppliers_tgs_api.services.parser.ProviderParserFactory;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierSearchEngineImpl implements SupplierSearchEngine {

    private final ProviderFactory providerFactory;
    private final ProviderParserFactory parserFactory;
    private final ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProductDTO> searchAll(String query) {

        UUID userId = getAuthenticatedUserId();

        List<ProductDTO> allResults = new ArrayList<>();

        for (ProviderName provider : ProviderName.values()) {

            try {

                ProviderService service =
                        providerFactory.getProvider(provider);

                List<ProductDTO> providerResults =
                        searchByProviderInternal(service, userId, query);

                allResults.addAll(providerResults);

            } catch (Exception e) {
                System.out.println("Provider failed: " + provider + " -> " + e.getMessage());
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

            System.out.println("[INVID LOCAL SEARCH] query = " + safeQuery);

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

                    String[] words = name.split(" ");
                    for (String w : words) {
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

    List<ProductDTO> allResults = new ArrayList<>();

    for (ProviderName provider : ProviderName.values()) {

        if (providers != null && !providers.isEmpty()) {

            Boolean enabled = providers.get(provider.name());

            if (!Boolean.TRUE.equals(enabled)) {
                continue;
            }
        }

        try {

            ProviderService service =
                    providerFactory.getProvider(provider);

            List<ProductDTO> providerResults =
                    searchByProviderInternal(service, userId, query);

            allResults.addAll(providerResults);

        } catch (Exception e) {
            System.out.println("Provider failed: " + provider + " -> " + e.getMessage());
        }
    }

    return filterResults(allResults, query);
}

}