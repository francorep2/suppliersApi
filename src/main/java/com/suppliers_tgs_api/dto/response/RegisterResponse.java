package com.suppliers_tgs_api.dto.response;

import java.util.UUID;

import com.suppliers_tgs_api.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de registro de usuario")
public class RegisterResponse {

    @Schema(
            description = "ID único del usuario",
            example = "c47e6af2-1234-5678-9abc-def123456789"
    )
    private UUID id;

    @Schema(
            description = "Nombre de usuario registrado",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Rol asignado al usuario",
            example = "ROLE_ADMIN"
    )
    private Role role;
}