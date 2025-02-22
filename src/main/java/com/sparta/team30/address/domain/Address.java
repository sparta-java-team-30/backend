package com.sparta.team30.address.domain;

import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.RequestUpdateAddressDTO;
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
    private Boolean isDeleted = false;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address(User user, RequestCreateAddressDTO requestCreateAddressDTO) {
        this.user = user;
        this.userPostcode = requestCreateAddressDTO.getUserPostcode();
        this.userAddress1 = requestCreateAddressDTO.getUserAddress1();
        this.userAddress2 = requestCreateAddressDTO.getUserAddress2();
        this.isDefault=true;
    }

    //테스트 용도
    public Address(UUID addressId, User user,boolean b) {
        this.addressId = addressId;
        this.user = user;
        this.updateDefault(b);
    }

    public void update(RequestUpdateAddressDTO requestUpdateAddressDTO) {
        this.userPostcode = requestUpdateAddressDTO.getUserPostcode();
        this.userAddress1 = requestUpdateAddressDTO.getUserAddress1();
        this.userAddress2 = requestUpdateAddressDTO.getUserAddress2();
    }

    public void deleteAddress(String deletedBy) {
        this.isDeleted = true;
        this.isDefault=false;
        super.delete(deletedBy);
    }

    public void updateDefault(boolean isDefault) {
        this.isDefault=isDefault;
    }
}
