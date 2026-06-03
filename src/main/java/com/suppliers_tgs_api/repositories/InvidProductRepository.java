package com.suppliers_tgs_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suppliers_tgs_api.model.InvidProduct;

@Repository
public interface InvidProductRepository
        extends JpaRepository<InvidProduct, String> {

    @Query("""
        SELECT p
        FROM InvidProduct p
        WHERE LOWER(p.title)
            LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.description)
            LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(p.partNumber)
            LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<InvidProduct> search(String query);

    List<InvidProduct> findByTitleContainingIgnoreCase(String title);

}
