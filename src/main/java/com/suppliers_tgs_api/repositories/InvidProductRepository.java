package com.suppliers_tgs_api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suppliers_tgs_api.model.InvidProduct;

@Repository
public interface InvidProductRepository
        extends JpaRepository<InvidProduct, UUID> {

    @Query("""
    SELECT p
    FROM InvidProduct p
    WHERE p.user.id = :userId
    AND (
        LOWER(p.title)
            LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.description)
            LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.partNumber)
            LIKE LOWER(CONCAT('%', :query, '%'))
    )
    """)
    List<InvidProduct> search(UUID userId, String query);

    List<InvidProduct> findByUserIdAndTitleContainingIgnoreCase(UUID userId,String title);

    Optional<InvidProduct> findByUserIdAndInvidProductId(UUID userId,String invidProductId);

}
