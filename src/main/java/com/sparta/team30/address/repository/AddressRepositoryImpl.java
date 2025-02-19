package com.sparta.team30.address.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.domain.QAddress;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.user.domain.QUser;
import com.sparta.team30.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepositoryCustom {

    private final JPAQueryFactory  jpaQueryFactory;

    @Override
    public List<ResponseAddressDTO> findAllByUsername(String username) {
        QAddress address = QAddress.address;
        QUser user = QUser.user;

        List<ResponseAddressDTO> fetch = jpaQueryFactory.select(Projections.constructor(ResponseAddressDTO.class,
                        address.addressId,
                        address.userPostcode,
                        address.userAddress1,
                        address.userAddress2,
                        address.isDeleted,
                        user.nickname
                )).from(address)
                .leftJoin(address.user, user)
                .orderBy(address.updatedAt.desc())
                .where(user.username.eq(username)).fetch();

        return fetch;
    }

    @Override
    public ResponseAddressDetailsDTO findByUsernameAndAddressId(String username, UUID addressId) {

        QAddress address = QAddress.address;
        QUser user = QUser.user;

        ResponseAddressDetailsDTO addressDetailsDTO = jpaQueryFactory.select(Projections.constructor(ResponseAddressDetailsDTO.class,
                        address.addressId,
                        address.userPostcode,
                        address.userAddress1,
                        address.userAddress2,
                        address.isDeleted,
                        user.nickname,
                        address.updatedAt
                )).from(address)
                .leftJoin(address.user, user)
                .where(user.username.eq(username).and(address.addressId.eq(addressId)))
                .fetchOne();

        return addressDetailsDTO;
    }
}
