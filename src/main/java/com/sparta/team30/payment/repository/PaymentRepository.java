package com.sparta.team30.payment.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p.paymentStatus FROM Payment p WHERE p.order.orderId=:orderId ORDER BY p.updatedAt DESC LIMIT 1")
    Optional<PaymentTypeEnum> findFirstByOrderOrderByUpdatedAtDesc(@Param("orderId") UUID orderId);


    List<Payment> findAllByOrderAndIsDeletedFalseOrderByUpdatedAtDesc(Order order);
}
