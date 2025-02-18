package com.sparta.team30.store.domain;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_store")
public class Store extends BaseEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR) //H2?
    @Column(name = "store_id", nullable = false, updatable = false, unique = true)
    private UUID storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "store_phone", nullable = false)
    private String storePhone;

    @Column(name = "store_postcode", nullable = false)
    private String storePostcode;

    @Column(name = "store_address1", nullable = false)
    private String storeAddress1;

    @Column(name = "store_address2")
    private String storeAddress2;

    @Column(name = "store_grade", nullable = false, columnDefinition = "Double DEFAULT 0")
    private double storeGrade = 0;

    @Column(name = "is_approved", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isApproved = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;
    

}
