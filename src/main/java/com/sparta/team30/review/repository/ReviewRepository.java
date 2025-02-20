package com.sparta.team30.review.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    //중복
    boolean existsByOrderId(Order orderId);

    @EntityGraph(value = "Review.withUserAndStore", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Review r WHERE r.storeId.storeId = :storeId " +
            "AND r.isDeleted = false " +
            "AND (:keyword IS NULL OR LOWER(r.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Review> findAllByStoreIdAndIsDeletedFalseWithKeyword(
            @Param("storeId") UUID storeId,
            @Param("keyword") String keyword,
            Pageable pageable);



    Optional<Review> findByReviewIdAndIsDeletedFalse(UUID storeId);


}
