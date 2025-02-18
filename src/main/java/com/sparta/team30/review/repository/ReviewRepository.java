package com.sparta.team30.review.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.store.domain.Store;
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
    //N+1 방지 Fatch Join
    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.user " +
            "WHERE r.storeId = :storeId AND r.isDeleted = false")
    List<Review> findAllByStoreAndIsDeletedFalse(@Param("storeId") Store store);
    List<Review> storeId(Store storeId);

    Optional<Review> findByStoreIdAndIsDeletedFalse(Store storeId);

    //소프트삭제 처리 
    @Modifying
    @Transactional
    @Query("update Review r SET r.isDeleted=true where r.reviewId= :reviewId")
    void deleteByReviewId(UUID reviewId);

    //평점계산
    Double calculatingAvgRating(UUID StoreId);
    int countByStoreIdAndIsDeletedFalse(Store storeId);
}
