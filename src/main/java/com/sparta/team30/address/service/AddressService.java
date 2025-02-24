package com.sparta.team30.address.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.RequestUpdateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.repository.AddressRepository;
import com.sparta.team30.common.exception.AddressAccessDeniedException;
import com.sparta.team30.common.exception.AddressNotFoundException;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void addAddress(UserDetails userDetails, RequestCreateAddressDTO requestCreateAddressDTO) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        clearOtherAddressDefault(userDetails.getUsername());
        addressRepository.save(new Address(user, requestCreateAddressDTO));
    }

    //기본 배송지로 설정
    @Transactional
    public void setDefaultAddress(UserDetails userDetails, UUID addressId) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("존재하지 않는 주소입니다."));

        clearOtherAddressDefault(userDetails.getUsername());

        address.updateDefault(true);
    }


    @Transactional
    public void updateAddress(UserDetails userDetails, UUID addressId, RequestUpdateAddressDTO requestUpdateAddressDTO) {

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("존재하지 않는 주소입니다."));

        if (!address.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new AddressAccessDeniedException("잘못된 접근입니다.");
        }

        address.update(requestUpdateAddressDTO);
    }

    @Transactional
    public void deleteAddress(UserDetails userDetails, UUID addressId) {

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("존재하지 않는 주소입니다."));

        if (!address.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new AddressAccessDeniedException("잘못된 접근입니다.");
        }

        address.deleteAddress(userDetails.getUsername());
    }


    private void clearOtherAddressDefault(String username) {
        long l = addressRepository.clearAllAdressFalse(username);
    }


}
