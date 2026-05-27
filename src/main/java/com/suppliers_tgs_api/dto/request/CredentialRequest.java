package com.suppliers_tgs_api.dto.request;

import java.util.UUID;

import com.suppliers_tgs_api.model.ProviderName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialRequest {

    private UUID providerId;

    private ProviderName providerName;

    private String username;

    private String password;

    private String apiKey;
}