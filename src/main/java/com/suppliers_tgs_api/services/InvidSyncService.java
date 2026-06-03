package com.suppliers_tgs_api.services;

import java.util.UUID;

import com.suppliers_tgs_api.model.UserProviderCredential;

public interface InvidSyncService {

    void syncAllProducts(
            UUID userId,
            UserProviderCredential credential
    );
}