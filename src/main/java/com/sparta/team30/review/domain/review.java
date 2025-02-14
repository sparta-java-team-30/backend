package com.sparta.team30.review.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;


import java.util.UUID;

@Entity
@Getter
@Table(name = "p_review")
public class review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false , updatable = false, unique = true )
    private UUID reviewId;

    @Column(nullable = false,columnDefinition = "INTEGER CHECK (score BETWEEN 1 AND 5)")
    private Integer score;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Store storeId;
    /*
    @OneToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Order orderId;
    */
}
