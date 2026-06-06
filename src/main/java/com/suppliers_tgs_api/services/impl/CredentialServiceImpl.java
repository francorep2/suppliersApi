package com.suppliers_tgs_api.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suppliers_tgs_api.dto.request.CredentialRequest;
import com.suppliers_tgs_api.dto.response.CredentialResponse;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.repositories.UserRepository;
import com.suppliers_tgs_api.services.CredentialService;
import com.suppliers_tgs_api.services.EncryptionService;
import com.suppliers_tgs_api.services.ProviderService;
import com.suppliers_tgs_api.services.impl.providers.CredentialValidator;
import com.suppliers_tgs_api.services.impl.providers.ProviderFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final UserRepository userRepository;
    private final UserProviderCredentialRepository credentialRepository;
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ProviderFactory providerFactory;

    @Override
    public CredentialResponse saveOrUpdate(UUID userId, CredentialRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(
                                userId,
                                request.getProviderName()
                        )
                        .orElse(new UserProviderCredential());

        credential.setUser(user);
        credential.setProviderName(request.getProviderName());

        ProviderName provider = request.getProviderName();

        Map<String, Object> credentials =
                request.getCredentials();

        ProviderService service = providerFactory.getProvider(provider);
        ((CredentialValidator) service).validate(credentials);

        if (credentials.containsKey("password")) {

            String password =
                    credentials.get("password").toString();

            credentials.put(
                    "password",
                    encryptionService.encrypt(password)
            );
        }

        if (credentials.containsKey("pass")) {

            String pass =
                    credentials.get("pass").toString();

            credentials.put(
                    "pass",
                    encryptionService.encrypt(pass)
            );
        }

        if (credentials.containsKey("token")) {

            String token =
                    credentials.get("token").toString();

            credentials.put(
                    "token",
                    encryptionService.encrypt(token)
            );
        }

        if (credentials.containsKey("apiKey")) {

            String apiKey =
                    credentials.get("apiKey").toString();

            credentials.put(
                    "apiKey",
                    encryptionService.encrypt(apiKey)
            );
        }

        try {

            String json =
                    objectMapper.writeValueAsString(credentials);

            credential.setCredentialsJson(json);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Error converting credentials to JSON"
            );
        }

        credential.setUpdatedAt(LocalDateTime.now());

        if (credential.getId() == null) {
            credential.setCreatedAt(LocalDateTime.now());
        }

        credentialRepository.save(credential);

        return mapToResponse(credential);
    }

    @Override
    public List<CredentialResponse> getAllByUser(UUID userId) {

        return credentialRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CredentialResponse getByUserAndProvider(
            UUID userId,
            ProviderName providerName
    ) {

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(
                                userId,
                                providerName
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Credential not found"));

        return mapToResponse(credential);
    }

    @Override
    public void delete(UUID userId, ProviderName providerName) {

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(
                                userId,
                                providerName
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Credential not found"));

        credentialRepository.delete(credential);
    }

    private CredentialResponse mapToResponse(
            UserProviderCredential credential
    ) {

        CredentialResponse response = new CredentialResponse();

        response.setProviderName(
                credential.getProviderName()
        );

        response.setCredentialsJson(
                credential.getCredentialsJson()
        );

        return response;
    }

@Override
    public List<UserProviderCredential> getAllUserProviderCredentials(UUID userId) {
        return credentialRepository.findByUserId(userId);
    }

}