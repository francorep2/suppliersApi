package com.suppliers_tgs_api.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.dto.request.CredentialRequest;
import com.suppliers_tgs_api.dto.response.CredentialResponse;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.model.UserProviderCredential;
import com.suppliers_tgs_api.repositories.UserProviderCredentialRepository;
import com.suppliers_tgs_api.repositories.UserRepository;
import com.suppliers_tgs_api.services.CredentialService;
import com.suppliers_tgs_api.services.EncryptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final UserRepository userRepository;
    private final UserProviderCredentialRepository credentialRepository;
    private final EncryptionService encryptionService;

    @Override
    public CredentialResponse saveOrUpdate(UUID userId, CredentialRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(userId, request.getProviderName())
                        .orElse(new UserProviderCredential());

        credential.setUser(user);
        credential.setProviderName(request.getProviderName());

        ProviderName provider = request.getProviderName();

        switch (provider) {

            case NEW_BYTES -> {
                if (request.getUsername() == null || request.getPassword() == null) {
                    throw new RuntimeException("NEW_BYTES requires username and password");
                }
            }

            case NEW_TREE -> {
                if (request.getApiKey() == null) {
                    throw new RuntimeException("NEW_TREE requires apiKey");
                }
            }

            case ELIT -> {
                if (request.getExternalUserId() == null ||
                    request.getExternalToken() == null) {
                    throw new RuntimeException("ELIT requires externalUserId and externalToken");
                }
            }

            default -> throw new RuntimeException("Unsupported provider: " + provider);
        }

        credential.setUsername(request.getUsername());

        credential.setExternalUserId(request.getExternalUserId());

        if (request.getExternalToken() != null) {
            credential.setExternalToken(encryptionService.encrypt(request.getExternalToken()));
        }

        if (request.getApiKey() != null) {
            credential.setApiKey(encryptionService.encrypt(request.getApiKey()));
        }

        if (request.getPassword() != null) {
            credential.setPassword(encryptionService.encrypt(request.getPassword()));
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
    public CredentialResponse getByUserAndProvider(UUID userId, ProviderName providerName) {

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(userId, providerName)
                        .orElseThrow(() -> new RuntimeException("Credential not found"));

        return mapToResponse(credential);
    }

    @Override
    public void delete(UUID userId, ProviderName providerName) {

        UserProviderCredential credential =
                credentialRepository.findByUserIdAndProviderName(userId, providerName)
                        .orElseThrow(() -> new RuntimeException("Credential not found"));

        credentialRepository.delete(credential);
    }

    private CredentialResponse mapToResponse(UserProviderCredential credential) {

        CredentialResponse response = new CredentialResponse();

        response.setProviderName(credential.getProviderName());
        response.setUsername(credential.getUsername());

        response.setHasApiKey(credential.getApiKey() != null);
        response.setHasPassword(credential.getPassword() != null);

        return response;
    }
}