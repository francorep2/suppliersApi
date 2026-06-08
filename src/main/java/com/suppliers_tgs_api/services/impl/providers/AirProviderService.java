package com.suppliers_tgs_api.services.impl.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
public class AirProviderService implements ProviderService, CredentialValidator {

    private final UserProviderCredentialRepository repository;
    private final EncryptionService encryptionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<UUID, ProviderAuthContext> cache = new ConcurrentHashMap<>();

    private static final String BASE_URL =
            "https://api.air-intra.com/v2/";


    @Override
    public ProviderName getProviderName() {
        return ProviderName.AIR;
    }

    @Override
    public ProviderAuthContext login(UUID userId) {

        try {

            UserProviderCredential cred =
                    repository.findByUserIdAndProviderName(userId, ProviderName.AIR)
                            .orElseThrow();

            JsonNode node = objectMapper.readTree(cred.getCredentialsJson());

            String user = node.get("user").asText();
            String passEncript = node.get("pass").asText();

            String pass = encryptionService.decrypt(passEncript);
            
            String url = BASE_URL + "?user=" + user + "&pass=" + pass;

            Map response = restTemplate.getForObject(url, Map.class);

            String token = (String) response.get("token");

            ProviderAuthContext auth = new ProviderAuthContext(token);

            cache.put(userId, auth);

            return auth;

        } catch (Exception e) {
            throw new RuntimeException("AIR login failed", e);
        }
    }
    
    @Override
    public String getElementByName(UUID userId, String name) {

    try {

        ProviderAuthContext auth = login(userId);

        String token = auth.getSessionToken();
        System.out.println("AIR token: " + token);

        if (token == null || token.isBlank()) {
            throw new RuntimeException("AIR token is null");
        }

        String searchUrl =
                BASE_URL + "?q=articulos&page=0";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        // Opción 1 (actual)
        headers.setBearerAuth(token);

        Map<String, Object> body = new HashMap<>();
        body.put("texto", name);
        body.put("stock", "D");

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                searchUrl,
                HttpMethod.POST,
                request,
                String.class
        ).getBody();

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("AIR search failed: " + e.getMessage(), e);
    }
}

    @Override
    public void validate(Map<String, Object> credentials) {

        if (!credentials.containsKey("user") ||
            !credentials.containsKey("pass")) {

            throw new RuntimeException("AIR needs user + pass");
        }
    }
}