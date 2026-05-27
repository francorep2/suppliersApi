package com.suppliers_tgs_api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderParser;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ProductDTO> searchAll(String query) {

        UUID userId = getAuthenticatedUserId();

        System.out.println("[SEARCH_ALL] query = " + query);
        System.out.println("[SEARCH_ALL] userId = " + userId);

        List<ProductDTO> allResults = new ArrayList<>();

        for (ProviderName provider : ProviderName.values()) {

            try {
                System.out.println("[SEARCH_ALL] provider = " + provider);

                ProviderService service =
                        providerFactory.getProvider(provider);

                List<ProductDTO> providerResults =
                        searchByProviderInternal(service, userId, query);

                System.out.println("[SEARCH_ALL] provider results size = " + providerResults.size());

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

        System.out.println("[SEARCH_PROVIDER] provider = " + providerName);
        System.out.println("[SEARCH_PROVIDER] query = " + query);

        ProviderService service =
                providerFactory.getProvider(providerName);

        List<ProductDTO> results =
                searchByProviderInternal(service, userId, query);

        System.out.println("[SEARCH_PROVIDER] results size = " + results.size());

        return filterResults(results, query);
    }

    private List<ProductDTO> searchByProviderInternal(
            ProviderService service,
            UUID userId,
            String query
    ) {

        System.out.println("[INTERNAL] provider = " + service.getProviderName());
        System.out.println("[INTERNAL] query = " + query);

        String safeQuery = (query == null) ? "" : query;

        // =========================
        // INVID PAGINATION LOGIC
        // =========================
        if (service.getProviderName() == ProviderName.INVID) {

            List<ProductDTO> all = new java.util.ArrayList<>();
            String offset = "0";

            int maxPages = 10;
            int page = 0;

            while (page < maxPages) {

                String paginatedQuery = safeQuery + "&offset=" + offset;

                String rawResponse = service.getElementByName(userId, paginatedQuery);

                System.out.println("[INVID PAGINATION] page = " + page);
                System.out.println("[INVID PAGINATION] offset = " + offset);

                ProviderParser parser = parserFactory.getParser(service.getProviderName());
                all.addAll(parser.parse(rawResponse));

                try {
                    JsonNode json = objectMapper.readTree(rawResponse);
                    JsonNode next = json.get("next_page_url");

                    if (next == null || next.asText().isBlank()) {
                        break;
                    }

                    String nextUrl = next.asText();

                    if (!nextUrl.contains("offset=")) {
                        break;
                    }

                    offset = nextUrl.split("offset=")[1];

                    page++;

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ignored) {}

                } catch (Exception e) {
                    break;
                }
            }

            System.out.println("[INVID PAGINATION] total parsed = " + all.size());
            return all;
        }

        // =========================
        // DEFAULT BEHAVIOR (ALL OTHER PROVIDERS)
        // =========================

        String rawResponse = service.getElementByName(userId, safeQuery);

        System.out.println("[INTERNAL] rawResponse = " + rawResponse);

        ProviderParser parser = parserFactory.getParser(service.getProviderName());

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

                // ✔ match exact
                if (name.contains(q)) return true;

                // ✔ match por palabras
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

        return text
                .toLowerCase()
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
}