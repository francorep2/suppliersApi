package com.suppliers_tgs_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(
    name = "invid_products",
    indexes = {
        @Index(name = "idx_invid_title", columnList = "title"),
        @Index(name = "idx_invid_part_number", columnList = "partNumber"),
    }
)
public class InvidProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String invidProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String partNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private String stockStatus;

    private String imageUrl;

    private LocalDateTime lastSync;
}