package com.sparta.team30.order.domain;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.dto.RequestUpdateOrderDTO;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderTypeEnum orderType;

    @Column(name = "comment")
    private String comment;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false
    )
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    private List<Payment> payment;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false
    )
    private Address address;

    public Order(RequestCreateOrderDTO requestCreateOrderDTO, OrderTypeEnum orderType,int price, User user,Address address
                 ) {
        this.orderType = orderType;
        this.comment = requestCreateOrderDTO.getComment();
        this.orderStatus = "결제 전";
        this.isDeleted = false;
        this.price = price;
        this.user = user;
        this.address = address;
    }

    public void update(RequestUpdateOrderDTO orderDTO)
    {
        this.comment = orderDTO.getComment();
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void updateStatus(String orderStatus) {
        this.orderStatus=orderStatus;
    }

    public void deleteOrder(String deletedBy, String orderStatus) {
        super.delete(deletedBy);
        this.orderStatus=orderStatus;
        this.isDeleted = true;
    }
}
