package com.suppliers_tgs_api.services.impl;

import java.util.UUID;
import java.util.List;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.services.ProviderService;
import com.suppliers_tgs_api.services.SupplierSearchEngine;
import com.suppliers_tgs_api.services.impl.providers.ProviderFactory;
import com.suppliers_tgs_api.services.parser.ProviderParserFactory;
import com.suppliers_tgs_api.services.ProviderParser;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SupplierSearchEngineImpl implements SupplierSearchEngine {

    private final ProviderFactory providerFactory;
    private final ProviderParserFactory parserFactory;

    @Override
    public List<ProductDTO> searchAll(UUID userId, String query) {

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
    public List<ProductDTO> searchByProvider(UUID userId, ProviderName providerName, String query) {

        ProviderService service =
                providerFactory.getProvider(providerName);

        return searchByProviderInternal(service, userId, query);
    }

    private List<ProductDTO> searchByProviderInternal(
            ProviderService service,
            UUID userId,
            String query
    ) {

        String rawResponse =
                service.getElementByName(userId, query);

        ProviderParser parser =
                parserFactory.getParser(service.getProviderName());

        return parser.parse(rawResponse);
    }

    private List<ProductDTO> filterResults(List<ProductDTO> list, String query) {

        if (query == null || query.isBlank()) return list;

        String q = query.toLowerCase();

        return list.stream()
                .filter(p ->
                        (p.getName() != null && p.getName().toLowerCase().contains(q))
                                || (p.getRaw() != null && p.getRaw().toLowerCase().contains(q))
                )
                .toList();
    }
}