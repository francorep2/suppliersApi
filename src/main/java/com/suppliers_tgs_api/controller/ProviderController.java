package com.suppliers_tgs_api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.auth.security.CustomUserDetails;
import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.services.ProviderFacadeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderFacadeService facadeService;

    @GetMapping("/{providerName}/search")
    public String search(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable ProviderName providerName,
            @RequestParam String name
    ) {
        return facadeService.search(user.getId(), providerName, name);
    }
}
