package com.sparta.team30.payment.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findFirstByOrderOrderByUpdatedAtDesc(Order order);


    List<Payment> findAllByOrderAndIsDeletedFalseOrderByUpdatedAtDesc(Order order);
}
