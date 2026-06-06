package com.suppliers_tgs_api.services;

import java.util.List;
import java.util.UUID;

import com.suppliers_tgs_api.dto.request.CredentialRequest;
import com.suppliers_tgs_api.dto.response.CredentialResponse;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;

public interface CredentialService {

    CredentialResponse saveOrUpdate(UUID userId, CredentialRequest request);

    List<CredentialResponse> getAllByUser(UUID userId);

    CredentialResponse getByUserAndProvider(UUID userId, ProviderName providerName);

    void delete(UUID userId, ProviderName providerName);

    List<UserProviderCredential> getAllUserProviderCredentials(UUID userId);

}