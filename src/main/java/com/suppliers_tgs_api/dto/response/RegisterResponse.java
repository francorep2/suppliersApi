package com.suppliers_tgs_api.dto.response;

import java.util.UUID;

import com.suppliers_tgs_api.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterResponse {
    private UUID id;
    private String username;
    private Role role;
}