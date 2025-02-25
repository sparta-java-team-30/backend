package com.sparta.team30.review.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NamedEntityGraph(name = "Review.withUserAndStore",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("storeId"),
                @NamedAttributeNode("orderId")
        })
@Getter
@Setter
@NoArgsConstructor
@Table(name = "P_REVIEW")
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

    public Review(int score, String content, Store store, Order order,User user) {
        this.score = score;
        this.content = content;
        this.storeId = store;
        this.orderId = order;
        this.isDeleted = false;
        this.user = user;
    }


    public void deleteReview(String deletedBy) {
        super.delete(deletedBy);
        this.isDeleted = true;
    }
    public void updateReview(int score, String content) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("평점은 1 ~ 5 만 가능합니다.");
        }
        this.score = score;
        this.content = content;
    }



}
