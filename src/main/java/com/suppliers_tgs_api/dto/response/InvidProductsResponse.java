package com.suppliers_tgs_api.dto.response;

import java.util.List;

import com.suppliers_tgs_api.dto.ProductDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta de productos provenientes de Invid con información de paginación")
public class InvidProductsResponse {

    @Schema(
            description = "Lista de productos obtenidos",
            implementation = ProductDTO.class
    )
    private List<ProductDTO> productos;

    @Schema(
            description = "Cantidad total de páginas disponibles",
            example = "5"
    )
    private Integer totalPages;
}