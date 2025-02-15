package com.sparta.team30.address.domain;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_address")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "user_postcode", nullable = false)
    private String userPostcode;

    @Column(name = "user_address1", nullable = false)
    private String userAddress1;

    @Column(name = "user_address2")
    private String userAddress2;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
