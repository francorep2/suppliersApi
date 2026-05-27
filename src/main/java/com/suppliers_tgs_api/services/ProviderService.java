package com.suppliers_tgs_api.services;

import java.util.UUID;
import com.suppliers_tgs_api.model.ProviderAuthContext;
import com.suppliers_tgs_api.model.ProviderName;

public interface ProviderService {

    ProviderName getProviderName();

    String getElementByName(UUID userId, String name);

    ProviderAuthContext login(UUID userId);

}