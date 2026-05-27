package com.suppliers_tgs_api.services;

import java.util.UUID;

import com.suppliers_tgs_api.model.ProviderName;

public interface ProviderService {

    ProviderName getProviderName();

    String login(UUID userId);

    String getElementByName(UUID userId, String name);
}