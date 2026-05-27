package com.suppliers_tgs_api.services.impl.providers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Component
public class ProviderFactory {

    private final List<ProviderService> services;

    public ProviderFactory(List<ProviderService> services) {
        this.services = services;
    }

    public ProviderService getProvider(ProviderName name) {
        return services.stream()
                .filter(s -> s.getProviderName() == name)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }
}