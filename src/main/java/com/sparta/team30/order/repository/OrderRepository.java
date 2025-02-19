package com.sparta.team30.order.repository;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.dto.ResponseOrderHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> , OrderRepositoryCustom{
    Optional<Order> findByOrderIdAndIsDeletedFalse(UUID order);

}
