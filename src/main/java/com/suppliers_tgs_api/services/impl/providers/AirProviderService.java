package com.suppliers_tgs_api.services.impl.providers;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.services.EncryptionService;
import com.suppliers_tgs_api.services.ProviderService;
import com.suppliers_tgs_api.model.ProviderAuthContext;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

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

            if (cache.containsKey(userId)) {
                return cache.get(userId);
            }

            UserProviderCredential cred =
                    repository.findByUserIdAndProviderName(userId, ProviderName.AIR)
                            .orElseThrow();

            JsonNode node = objectMapper.readTree(cred.getCredentialsJson());

            String user = node.get("user").asText();
            String pass = node.get("pass").asText();

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
        UserProviderCredential cred =
                repository.findByUserIdAndProviderName(userId, ProviderName.AIR)
                        .orElseThrow();

        JsonNode node =
                objectMapper.readTree(cred.getCredentialsJson());

        String user = node.get("user").asText();
        String pass = node.get("pass").asText();

        String loginUrl = BASE_URL + "?user=" + user + "&pass=" + pass;

        Map loginResponse =
                restTemplate.getForObject(loginUrl, Map.class);

        String token = (String) loginResponse.get("token");

        if (token == null) {
            throw new RuntimeException("AIR login returned null token");
        }

        String searchUrl = BASE_URL + "?q=" + name;
        System.out.println(" ::::::::: AIR token: " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
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