package com.sparta.team30.review.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    //중복
    boolean existsByOrderId(Order orderId);

    @EntityGraph(value = "Review.withUserAndStore", type = EntityGraph.EntityGraphType.FETCH)
    Page<Review> findAllByStoreIdAndIsDeletedFalse(@Param("storeId") UUID storeId, Pageable pageable);



    Optional<Review> findByReviewIdAndIsDeletedFalse(UUID storeId);


}
