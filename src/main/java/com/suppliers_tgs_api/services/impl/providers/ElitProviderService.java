package com.suppliers_tgs_api.services.impl.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class ElitProviderService implements ProviderService {

    private final UserProviderCredentialRepository repository;
    private final RestTemplate restTemplate;
    private final EncryptionService encryptionService;

    private static final String BASE_URL =
            "https://clientes.elit.com.ar/v1/api/productos";

    @Override
    public ProviderName getProviderName() {
        return ProviderName.ELIT;
    }

    @Override
    public String login(UUID userId) {
        return "NO_LOGIN"; // ELIT no usa login dinámico
    }

   @Override
public String getElementByName(UUID userId, String name) {

    UserProviderCredential cred =
            repository.findByUserIdAndProviderName(
                    userId,
                    ProviderName.ELIT
            ).orElseThrow();

    String url = BASE_URL +
            "?limit=100" +
            "&nombre=" + name;

    String token = encryptionService.decrypt(cred.getExternalToken());
    
    Map<String, Object> body = new HashMap<>();
    body.put("user_id", cred.getExternalUserId());
    body.put("token", token);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(body, headers);

    return restTemplate.postForObject(
            url,
            request,
            String.class
    );

}
}