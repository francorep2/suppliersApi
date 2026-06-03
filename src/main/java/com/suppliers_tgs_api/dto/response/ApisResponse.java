package com.suppliers_tgs_api.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura base de respuesta de la API")
public class ApisResponse<T> {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo de la respuesta", example = "Operation successful")
    private String message;

    @Schema(description = "Datos de respuesta (depende del endpoint)")
    private T data;
}