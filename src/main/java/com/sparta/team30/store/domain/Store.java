package com.sparta.team30.store.domain;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.store.dto.StoreRequestDto;
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

    @Column(name = "store_grade", nullable = false)
    private double storeGrade = 0.0;

    @Column(name ="store_review_count", nullable = false)
    private int storeReviewCount = 0;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public void addReviewScore(double score) {
        if (this.storeReviewCount == 0) {
            // 첫 리뷰
            // score를 소수점 한 자리로 반올림 후 저장
            double roundedScore = Math.round(score * 10) / 10.0;
            this.storeGrade = roundedScore;
            this.storeReviewCount = 1;
        } else {
            // 기존 리뷰가 있는 경우
            double newAverage =
                    ((this.storeGrade * this.storeReviewCount) + score) / (this.storeReviewCount + 1);
            // 계산된 평균을 소수점 한 자리로 반올림
            double roundedAverage = Math.round(newAverage * 10) / 10.0;
            this.storeGrade = roundedAverage;
            this.storeReviewCount++;
        }
    }


    public Store(Category category, StoreRequestDto requestDto) {
        this.category = category;
        this.storeName = requestDto.getStoreName();
        this.storePhone = requestDto.getStorePhone();
        this.storePostcode = requestDto.getStorePostcode();
        this.storeAddress1 = requestDto.getStoreAddress1();
        this.storeAddress2 = requestDto.getStoreAddress2();
    }

    public void approve() {
        this.isApproved = true;
    }

    @Override
    public void delete(String deletedBy) {
        super.delete(deletedBy);
        this.isDeleted = true;
    }

}
