package com.suppliers_tgs_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DolarResponse {
    private Integer compra;
    private Integer venta;
    private String fechaActualizacion;
}