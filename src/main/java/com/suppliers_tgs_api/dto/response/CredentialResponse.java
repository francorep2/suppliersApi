package com.suppliers_tgs_api.dto.response;

import com.suppliers_tgs_api.model.ProviderName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de credenciales de proveedor")
public class CredentialResponse {

    @Schema(
            description = "Nombre del proveedor",
            example = "INVID"
    )
    private ProviderName providerName;

    @Schema(
            description = "Credenciales del proveedor en formato JSON serializado",
            example = "{\"username\":\"userAdmin\",\"password\":\"a2dfg\"}"
    )
    private String credentialsJson;
}