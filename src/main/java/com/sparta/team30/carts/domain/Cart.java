package com.sparta.team30.carts.domain;


import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_cart")
public class Cart {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "cart_id", updatable = false, nullable = false)
    private UUID cartId;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
}
