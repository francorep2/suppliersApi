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
import com.suppliers_tgs_api.repositories.InvidProductRepository;
import com.suppliers_tgs_api.dto.ProductDTO;
import com.suppliers_tgs_api.model.InvidProduct;
import com.suppliers_tgs_api.services.parser.InvidParser;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvidProviderService implements ProviderService, CredentialValidator {

    private final UserProviderCredentialRepository repository;
    private final ObjectMapper objectMapper;
    private final EncryptionService encryptionService;
    private final RestTemplate restTemplate;
    private final InvidProductRepository invidProductRepository;
    private final InvidParser invidParser;

    private static final String LOGIN_URL =
            "https://www.invidcomputers.com/api/v1/auth.php";

    private static final String BASE_URL =
            "https://www.invidcomputers.com/api/v1/articulo.php";

    @Override
    public ProviderName getProviderName() {
        return ProviderName.INVID;
    }

    @Override
    public ProviderAuthContext login(UUID userId) {

        try {

            UserProviderCredential cred =
                    repository.findByUserIdAndProviderName(userId, ProviderName.INVID)
                            .orElseThrow();

            JsonNode node =
                    objectMapper.readTree(cred.getCredentialsJson());

            String username = node.get("username").asText();
            String encryptedPassword = node.get("password").asText();
            String password = encryptionService.decrypt(encryptedPassword);

            Map<String, Object> body = Map.of(
                    "username", username,
                    "password", password
            );

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(LOGIN_URL, body, Map.class);

            Map respBody = response.getBody();

            if (respBody == null || (int) respBody.get("status") != 1) {
                throw new RuntimeException("INVID login failed");
            }

            String token = (String) respBody.get("access_token");

            return new ProviderAuthContext(token);

        } catch (Exception e) {
            e.printStackTrace();
                throw new RuntimeException("INVID login failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String getElementByName(UUID userId, String name) {

    ProviderAuthContext auth = login(userId);

    String url = BASE_URL;

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
    public void validate(Map<String, Object> credentials) throws RuntimeException {
        if (!credentials.containsKey("username") ||
                !credentials.containsKey("password")) {

                throw new RuntimeException("INVID needs username + password");
        }
    }

   public JsonNode callEndpoint(UUID userId,String url) {

    ProviderAuthContext auth =
            login(userId);

    HttpHeaders headers =
            new HttpHeaders();

    headers.setBearerAuth(
            auth.getLoginToken()
    );

    HttpEntity<Void> request =
            new HttpEntity<>(headers);

    String body =
            restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class
            ).getBody();

    try {
        return objectMapper.readTree(body);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
   }

   
}