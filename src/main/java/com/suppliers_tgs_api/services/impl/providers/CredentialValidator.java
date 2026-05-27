package com.suppliers_tgs_api.services.impl.providers;
import java.util.Map;

public interface CredentialValidator {
    void validate(Map<String, Object> credentials);
}