package com.sparta.team30.carts.repository;

import com.sparta.team30.carts.domain.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Page<CartItem> findByCartId(UUID cartId);
}
