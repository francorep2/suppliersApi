package com.suppliers_tgs_api.dto.response;

import com.suppliers_tgs_api.model.ProviderName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CredentialResponse {

    private ProviderName providerName;

    private String credentialsJson;

}