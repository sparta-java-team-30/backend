package com.sparta.team30.products.domain;

import com.sparta.team30.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product_detail")
public class ProductDetail extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)", name = "product_detail_id")
    private UUID productDetailId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_detail_content")
    private String productDetailContent;

    @Column
    private boolean isDeleted;

    @Builder
    public ProductDetail(Product product, String productDetailContent, boolean isDeleted) {
        this.product = product;
        this.productDetailContent = productDetailContent;
        this.isDeleted = isDeleted;
    }

    public void productDetailDelete(boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}
