package com.suppliers_tgs_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "invid_products",
    indexes = {
        @Index(name = "idx_invid_title", columnList = "title"),
        @Index(name = "idx_invid_part_number", columnList = "partNumber"),
        @Index(name = "idx_invid_brand", columnList = "brand")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class InvidProduct {

    @Id
    private String id;

    private String title;

    private String partNumber;

    @Column(length = 5000)
    private String description;

    private BigDecimal price;

    private String stockStatus;

    @Column(length = 5000)
    private String imageUrl;

    private LocalDateTime lastSync;
}
