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
@Table(name = "p_product_detail_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailHistory extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)",name = "product_detail_history_id", updatable = false, nullable = false)
    private UUID productDetailHistoryId;

    @Column(nullable = false)
    private String productDetailRequest;

    @Column
    private String productDetailResponse;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Builder
    public ProductDetailHistory(ProductDetail productDetail,String productDetailRequest, String productDetailResponse) {
        this.productDetail = productDetail;
        this.productDetailRequest = productDetailRequest;
        this.productDetailResponse = productDetailResponse;
    }
}
