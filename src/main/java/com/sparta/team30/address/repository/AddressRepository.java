package com.sparta.team30.address.repository;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>, AddressRepositoryCustom {


    Optional<Address> findFirstByOrderByUpdatedAtDesc();
}
