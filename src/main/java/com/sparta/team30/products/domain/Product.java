package com.sparta.team30.products.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(length = 50)
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(length = 50)
    private String deletedBy;

    @Column(nullable = false)
    private boolean isVisible;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private String storeId;
}