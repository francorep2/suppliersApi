package com.suppliers_tgs_api.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.impl.providers.ProviderFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProviderFacadeService {

    private final ProviderFactory factory;

    public String search(UUID userId, ProviderName provider, String name) {

        ProviderService service = factory.getProvider(provider);

        return service.getElementByName(userId, name);
    }
}