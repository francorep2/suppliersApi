package com.suppliers_tgs_api.services.impl.providers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.services.EncryptionService;
import com.suppliers_tgs_api.services.ProviderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewBytesProviderService implements ProviderService {

    private final UserProviderCredentialRepository credentialRepository;
    private final EncryptionService encryptionService;
    private final RestTemplate restTemplate;

    private static final String AUTH_URL =
            "https://api.nb.com.ar/v1/auth/login";

    private static final String BASE_URL =
            "https://api.nb.com.ar/v1/";

    @Override
    public ProviderName getProviderName() {
        return ProviderName.NEW_BYTES;

    }

    @Override
    public String login(UUID userId) {

        UserProviderCredential cred =
                credentialRepository.findByUserIdAndProviderName(
                        userId,
                        ProviderName.NEW_BYTES
                ).orElseThrow();

        String user = cred.getUsername();
        String pass = encryptionService.decrypt(cred.getPassword());

        Map<String, Object> body = Map.of(
                "user", user,
                "password", pass,
                "mode", "api"
        );

        Map response = restTemplate.postForObject(
                AUTH_URL,
                body,
                Map.class
        );

        return (String) response.get("token");
    }

    @Override
    public String getElementByName(UUID userId, String name) {

        String token = login(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

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
}
