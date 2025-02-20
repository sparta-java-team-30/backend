package com.sparta.team30.payment.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponsePGPaymentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentTypeEnum paymentStatus;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Payment(Order order, RequestPaymentByOrderId requestPaymentByOrderId) {
        this.paymentPrice = order.getPrice();
        this.paymentType = requestPaymentByOrderId.getPaymentType();
        this.paymentStatus = PaymentTypeEnum.PENDING;
        this.order = order;
        this.isDeleted = false;
    }

    public Boolean update(ResponsePGPaymentDTO responsePGPaymentDTO) {
        if (responsePGPaymentDTO.isSuccess()) {
            this.paymentStatus = PaymentTypeEnum.COMPLETED;
            return true;
        }
        else{
            this.paymentStatus = PaymentTypeEnum.FAILED;
            return false;
        }

    }

    public void deletePayment(String deletedBy, PaymentTypeEnum paymentTypeEnum) {
        super.delete(deletedBy);
        this.paymentStatus = paymentTypeEnum;
        this.isDeleted = true;
    }
}
