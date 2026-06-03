package com.suppliers_tgs_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Representación de un producto unificado entre proveedores")
public class ProductDTO {

    @Schema(
            description = "Nombre del proveedor que devuelve el producto",
            example = "NEW_BYTES"
    )
    private String provider;

    @Schema(
            description = "Nombre del producto",
            example = "PROCESADOR AMD (AM4) RYZEN 7 5700G"
    )
    private String name;

    @Schema(
            description = "Precio del producto en USD",
            example = "120.00"
    )
    private String price;

    @Schema(
            description = "URL de la imagen del producto",
            example = "https://example.com/image.jpg"
    )
    private String imageUrl;

    @Schema(
            description = "ID externo del producto en el proveedor",
            example = "ABC123"
    )
    private String externalId;
}