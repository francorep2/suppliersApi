package com.suppliers_tgs_api.services.impl.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.services.EncryptionService;
import com.suppliers_tgs_api.services.ProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.suppliers_tgs_api.model.ProviderAuthContext;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ElitProviderService implements ProviderService, CredentialValidator {

    private final UserProviderCredentialRepository repository;
    private final RestTemplate restTemplate;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL =
            "https://clientes.elit.com.ar/v1/api/productos";

    @Override
    public ProviderName getProviderName() {
        return ProviderName.ELIT;
    }

    @Override
    public ProviderAuthContext login(UUID userId) {
        return new ProviderAuthContext("NO_LOGIN"); 
    }

@Override
public String getElementByName(UUID userId, String name) {

    try {

        UserProviderCredential cred =
                repository.findByUserIdAndProviderName(
                        userId,
                        ProviderName.ELIT
                ).orElseThrow();

        JsonNode node =
                objectMapper.readTree(
                        cred.getCredentialsJson()
                );

        Long externalUserId =
                node.get("user_id").asLong();

        String encryptedToken =
                node.get("token").asText();

        String token =
                encryptionService.decrypt(encryptedToken);

        String url = BASE_URL +
                "?limit=100" +
                "&nombre=" + name;

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", externalUserId);
        body.put("token", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                org.springframework.http.MediaType.APPLICATION_JSON
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        return restTemplate.postForObject(
                url,
                request,
                String.class
        );

    } catch (Exception e) {

        throw new RuntimeException(
                "Error fetching ELIT products",
                e
        );
    }
}

        @Override
        public void validate(Map<String, Object> credentials) {

        if (!credentials.containsKey("user_id") ||
                !credentials.containsKey("token")) {

                throw new RuntimeException("ELIT needs user_id + token");
        }
        }

}
