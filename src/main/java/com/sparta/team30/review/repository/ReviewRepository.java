package com.sparta.team30.review.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    //중복
    boolean existsByOrderId(UUID orderId);
}
