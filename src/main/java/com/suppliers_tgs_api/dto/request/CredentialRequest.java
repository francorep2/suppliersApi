package com.suppliers_tgs_api.dto.request;

import java.util.Map;

import com.suppliers_tgs_api.model.ProviderName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CredentialRequest {

    private ProviderName providerName;

    private Map<String, Object> credentials;
}