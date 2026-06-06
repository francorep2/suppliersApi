package com.suppliers_tgs_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.dto.request.CredentialRequest;
import com.suppliers_tgs_api.dto.response.CredentialResponse;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.CredentialService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping
    public CredentialResponse saveOrUpdate(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CredentialRequest request
    ) {
        return credentialService.saveOrUpdate(user.getId(), request);
    }

    @GetMapping("/me")
    public List<CredentialResponse> getMyCredentials(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return credentialService.getAllByUser(user.getId());
    }

    @GetMapping("/{providerName}")
    public CredentialResponse getByProvider(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName
    ) {
        return credentialService.getByUserAndProvider(user.getId(), providerName);
    }

    @DeleteMapping("/{providerName}")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName
    ) {
        credentialService.delete(user.getId(), providerName);
    }
    
    @GetMapping("/user/{userId}")
@PreAuthorize("hasRole('ADMIN')")
public List<CredentialResponse> getCredentialsByUserId(
        @PathVariable UUID userId
) {
    return credentialService.getAllByUser(userId);
}
}