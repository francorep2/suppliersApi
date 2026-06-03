package com.suppliers_tgs_api.services.impl.providers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.model.ProviderAuthContext;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.services.EncryptionService;
import com.suppliers_tgs_api.services.ProviderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewBytesProviderService implements ProviderService, CredentialValidator {

    private final UserProviderCredentialRepository credentialRepository;
    private final EncryptionService encryptionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String AUTH_URL =
            "https://api.nb.com.ar/v1/auth/login";

    private static final String BASE_URL =
            "https://api.nb.com.ar/v1/";

    @Override
    public ProviderName getProviderName() {
        return ProviderName.NEW_BYTES;
    }

    @Override
    public ProviderAuthContext login(UUID userId) {

        try {

            UserProviderCredential cred =
                    credentialRepository.findByUserIdAndProviderName(
                            userId,
                            ProviderName.NEW_BYTES
                    ).orElseThrow();

            JsonNode node =
                    objectMapper.readTree(cred.getCredentialsJson());

            String username =
                    node.get("user").asText();

            String encryptedPassword =
                    node.get("password").asText();

            String password =
                    encryptionService.decrypt(encryptedPassword);

            Map<String, Object> body = Map.of(
                    "user", username,
                    "password", password,
                    "mode", "api"
            );

            Map response =
                    restTemplate.postForObject(
                            AUTH_URL,
                            body,
                            Map.class
                    );

            String token = (String) response.get("token");

            return new ProviderAuthContext(token);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error authenticating with NEW_BYTES",
                    e
            );
        }
    }

    @Override
    public String getElementByName(UUID userId, String name) {

        ProviderAuthContext auth = login(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth.getLoginToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String url = BASE_URL + "?title=" + name;

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        String.class
                );

        return response.getBody();
    }

    @Override
public void validate(Map<String, Object> credentials) {

    if (!credentials.containsKey("user") ||
        !credentials.containsKey("password")) {

        throw new RuntimeException("NEW_BYTES needs user + password");
    }
}
}