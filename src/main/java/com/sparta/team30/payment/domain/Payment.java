package com.sparta.team30.payment.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.domain.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_payment")
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "payment_price", nullable = false)
    private int paymentPrice;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_count", nullable = false)
    private int paymentCount;

    @Column(name = "payment_status", nullable = false)
    private PaymentTypeEnum paymentTypeEnum;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
