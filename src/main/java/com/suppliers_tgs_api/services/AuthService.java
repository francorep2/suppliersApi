package com.suppliers_tgs_api.services;

import com.suppliers_tgs_api.dto.LoginRequest;
import com.suppliers_tgs_api.dto.LoginResponse;
import com.suppliers_tgs_api.dto.RegisterRequest;
import com.suppliers_tgs_api.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}