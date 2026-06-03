package com.suppliers_tgs_api.dto.request;

import java.util.Map;

import com.suppliers_tgs_api.model.ProviderName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request para crear o actualizar credenciales de un proveedor")
public class CredentialRequest {

    @Schema(
            description = "Nombre del proveedor",
            example = "INVID"
    )
    private ProviderName providerName;

    @Schema(
            description = "Credenciales dinámicas del proveedor (keys variables según proveedor)",
            example = "{\"apiKey\": \"123456\", \"secret\": \"abcde\"}"
    )
    private Map<String, Object> credentials;
}