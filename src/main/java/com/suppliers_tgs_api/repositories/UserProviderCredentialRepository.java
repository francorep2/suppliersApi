package com.suppliers_tgs_api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suppliers_tgs_api.model.ProviderName;
import com.suppliers_tgs_api.model.UserProviderCredential;

@Repository
public interface UserProviderCredentialRepository extends JpaRepository<UserProviderCredential, UUID> {

    Optional<UserProviderCredential> findByUserIdAndProviderName(UUID userId, ProviderName providerName);

    List<UserProviderCredential>findByUserId(UUID userId);

    

}