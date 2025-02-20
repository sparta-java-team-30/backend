package com.sparta.team30.address.repository;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepositoryCustom {


    List<ResponseAddressDTO> findAllByUsername(String username);

    ResponseAddressDetailsDTO findByUsernameAndAddressId(String username, UUID addressId);

    long clearAllAdressFalse(String username);

    Address findByUsernameAndAddressIsDefault(String username);
}
