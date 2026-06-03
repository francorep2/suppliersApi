package com.suppliers_tgs_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request para autenticación de usuario")
public class LoginRequest {

    @Schema(
            description = "Nombre de usuario o email",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Contraseña del usuario",
            example = "123456"
    )
    private String password;
}