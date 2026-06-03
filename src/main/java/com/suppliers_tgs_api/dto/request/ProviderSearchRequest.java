package com.suppliers_tgs_api.dto.request;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request para búsqueda filtrada por proveedores")
public class ProviderSearchRequest {

    @Schema(
            description = "Texto de búsqueda",
            example = "Procesador"
    )
    private String query;

    @Schema(
            description = "Mapa de proveedores habilitados para la búsqueda (true = activo, false = deshabilitado)",
            example = "{\"INVID\": true, \"AIR\": false}"
    )
    private Map<String, Boolean> providers;
}