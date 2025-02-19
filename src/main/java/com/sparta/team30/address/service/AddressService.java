package com.sparta.team30.address.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.repository.AddressRepository;
import com.sparta.team30.common.exception.AddressNotFoundException;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    public List<ResponseAddressDTO> getAllAddress(UserDetails userDetails) {

        List<ResponseAddressDTO> addressDTOList = addressRepository.findAllByUsername(userDetails.getUsername());

        return addressDTOList;
    }

    public ResponseAddressDetailsDTO getAddressDetails(UserDetails userDetails, UUID addressId) {


        ResponseAddressDetailsDTO addressDetailsDTO = addressRepository.findByUsernameAndAddressId(userDetails.getUsername(), addressId);
        if(addressDetailsDTO == null) {
            throw new AddressNotFoundException("존재하지 않는 주소입니다.");
        }

        return addressDetailsDTO;
    }

    public void addAddress(UserDetails userDetails, RequestCreateAddressDTO requestCreateAddressDTO) {

        User user = userRepository.findByUsername(userDetails.getPassword()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        addressRepository.save(new Address(user, requestCreateAddressDTO));
    }

}
