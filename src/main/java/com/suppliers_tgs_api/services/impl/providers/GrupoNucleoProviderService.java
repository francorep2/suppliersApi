package com.suppliers_tgs_api.services.impl.providers;

import java.util.UUID;

import com.suppliers_tgs_api.model.ProviderAuthContext;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.services.ProviderService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.services.EncryptionService;
import lombok.RequiredArgsConstructor;
import com.suppliers_tgs_api.services.impl.providers.CredentialValidator;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@Service
@RequiredArgsConstructor
public class GrupoNucleoProviderService implements ProviderService, CredentialValidator{

        private final UserProviderCredentialRepository repository;
        private final ObjectMapper objectMapper;
        private final RestTemplate restTemplate;
        private final EncryptionService encryptionService;

        @Override
        public ProviderName getProviderName() {
            return ProviderName.GRUPO_NUCLEO;
        }
    
        @Override
        public String getElementByName(UUID userId, String name) {

            ProviderAuthContext auth = login(userId);

            String url = "https://api.gruponucleosa.com/API_V1/GetCatalog";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(auth.getLoginToken());

            HttpEntity<Void> request = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class
            ).getBody();
        }
    
        @Override
        public ProviderAuthContext login(UUID userId) {

            try {

                UserProviderCredential cred =
                        repository.findByUserIdAndProviderName(userId, ProviderName.GRUPO_NUCLEO)
                                .orElseThrow();

                JsonNode node =
                        objectMapper.readTree(cred.getCredentialsJson());

                int id = node.get("id").asInt();
                String username = node.get("username").asText();
                String password = node.get("password").asText(); 

                String decryptedPassword = encryptionService.decrypt(password);

                Map<String, Object> body = Map.of(
                        "id", id,
                        "username", username,
                        "password", decryptedPassword
                );

                String url = "https://api.gruponucleosa.com/Authentication/Login";

                HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

                ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

                String token = response.getBody();

                if (token == null) {
                    throw new RuntimeException("GRUPO NUCLEO login returned null token");
                }

                return new ProviderAuthContext(token);

            } catch (Exception e) {
                e.printStackTrace();
                    throw new RuntimeException("GRUPO NUCLEO login failed: " + e.getMessage(), e);
            }
        }

        @Override
        public void validate(Map<String, Object> credentials) throws RuntimeException {


        }
}
