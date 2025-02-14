package com.sparta.team30.products.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID productId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Column(length = 50)
    private String updatedBy;

    @Column(length = 50)
    private String deletedBy;

    @Column(nullable = false)
    private boolean isVisible;

    @Column(nullable = false)
    private boolean isDeleted;

//    @ManyToOne
//    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
//    private Store store;

//    public void softDelete(String deletedBy) {
//        super.softDelete();
//        this.deletedBy = deletedBy;
//    }
}