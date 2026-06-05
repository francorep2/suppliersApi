package com.suppliers_tgs_api.services;

import com.suppliers_tgs_api.dto.request.LoginRequest;
import com.suppliers_tgs_api.dto.request.RegisterRequest;
import com.suppliers_tgs_api.dto.response.LoginResponse;
import com.suppliers_tgs_api.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);


}