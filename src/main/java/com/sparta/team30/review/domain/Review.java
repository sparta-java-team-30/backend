package com.sparta.team30.review.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID reviewId;

    @Column(nullable = false, columnDefinition = "INTEGER CHECK (score BETWEEN 1 AND 5)")
    private int score;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Store storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Order orderId;

    public Review(int score, String content, User user, Store store, Order order) {
        this.score = score;
        this.content = content;
        this.user = user;
        this.storeId = store;
        this.orderId = order;
        this.isDeleted = false;
    }

    // 새로 추가된 생성자
    public Review(UUID reviewId, int score, String content, User user, Store store, Order order) {

            this.reviewId = reviewId;
            this.score = score;
            this.content = content;
            this.user = user;
            this.storeId = store;
            this.orderId = order;
            this.isDeleted = false;



    }
}
