package com.suppliers_tgs_api.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request para operaciones administrativas sobre usuarios")
public class UserRequest {

    @Schema(
            description = "ID del usuario",
            example = "c47e6af2-1234-5678-9abc-def123456789"
    )
    private UUID userId;

    @Schema(
            description = "Estado activo del usuario",
            example = "true"
    )
    private Boolean active;

    @Schema(
            description = "Fecha de expiración del usuario",
            example = "2026-12-31"
    )
    private LocalDate endDate;
}