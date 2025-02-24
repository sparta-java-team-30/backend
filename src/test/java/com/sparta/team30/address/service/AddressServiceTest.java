package com.sparta.team30.address.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.RequestUpdateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.repository.AddressRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("배송지 조회 테스트")
    class GetAddress {
        @Test
        @DisplayName("배송지 목록 조회 테스트")
        void getAllAddress() {

            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            ResponseAddressDTO responseAddressDTO = new ResponseAddressDTO(
                    UUID.randomUUID(), "12345", "서울", "광화문", false, "user1", true);
            ResponseAddressDTO responseAddressDTO2 = new ResponseAddressDTO(
                    UUID.randomUUID(), "12345", "서울", "광화문", false, "user2", false);
            List < ResponseAddressDTO> addressDTOList = List.of(responseAddressDTO, responseAddressDTO2);
            when(addressRepository.findAllByUsername(username)).thenReturn(addressDTOList);
            //when
            List<ResponseAddressDTO> allAddress = addressService.getAllAddress(userDetails);

            //then
            assertEquals(2, allAddress.size());
            assertEquals("user1", allAddress.get(0).getNickname());
        }

        @Test
        @DisplayName("배송지 상세 조회 테스트")
        void getAddressDetails() {
            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            ResponseAddressDetailsDTO responseAddressDTO = new ResponseAddressDetailsDTO(
                    UUID.randomUUID(), "12345", "서울", "광화문", false, "user1", true, LocalDateTime.now()
            );
            UUID addressId = UUID.randomUUID();
            when(addressRepository.findByUsernameAndAddressId(username,addressId)).thenReturn(responseAddressDTO);

            //when
            ResponseAddressDetailsDTO addressDetails = addressService.getAddressDetails(userDetails, addressId);

            //then
            assertEquals("user1", addressDetails.getNickname());
            assertEquals("광화문", addressDetails.getUserAddress2());
        }

    }
    //통합테스트로 해야하나...
    @Nested
    @DisplayName("배송지 생성,등록 테스트")
    class AddAddress{
        @Test
        @DisplayName("배송지 생성 테스트")
        void addAddress() {
            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            User user = User.builder().build();
            ResponseAddressDetailsDTO responseAddressDTO = new ResponseAddressDetailsDTO(
                    UUID.randomUUID(), "12345", "서울", "광화문", false, "user1", true, LocalDateTime.now()
            );
            UUID addressId = UUID.randomUUID();
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));
            RequestCreateAddressDTO requestCreateAddressDTO = new RequestCreateAddressDTO("12345", "서울시", "광화문");
            //when
            addressService.addAddress(userDetails, requestCreateAddressDTO);
            //then
            verify(addressRepository,times(1)).save(any(Address.class));
        }

        @Test
        @DisplayName("기본 배송지 등록 테스트")
        void setDefaultAddress() {
            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            User user = User.builder().build();
            UUID addressId = UUID.randomUUID();
            Address address = new Address(addressId,user,false);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(user));
            when(addressRepository.findById(addressId)).thenReturn(Optional.ofNullable(address));
            //when
            addressService.setDefaultAddress(userDetails, addressId);
            //then
            assertEquals(true,address.getIsDefault());
        }
    }

    @Nested
    @DisplayName("배송지 수정,삭제 테스트")
    class UpdateDeleteAddress {
        @Test
        @DisplayName("배송지 정보 수정 테스트")
        void updateAddress() {
            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            User user = User.builder().username("user").build();
            UUID addressId = UUID.randomUUID();
            Address address = new Address(addressId,user,false);
            when(addressRepository.findById(addressId)).thenReturn(Optional.ofNullable(address));
            RequestUpdateAddressDTO requestUpdateAddressDTO = new RequestUpdateAddressDTO("12345", "서울시", "광화문");
            //when
            addressService.updateAddress(userDetails, addressId, requestUpdateAddressDTO);
            //then
            assertEquals("서울시",requestUpdateAddressDTO.getUserAddress1());
            assertEquals("광화문",requestUpdateAddressDTO.getUserAddress2());
        }

        @Test
        @DisplayName("배송지 삭제 테스트")
        void deleteAddress() {
            //given
            String username = "user";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(username);
            User user = User.builder().username("user").build();
            UUID addressId = UUID.randomUUID();
            Address address = new Address(addressId,user, false);
            when(addressRepository.findById(addressId)).thenReturn(Optional.ofNullable(address));
            //when
            addressService.deleteAddress(userDetails, addressId);
            //then
            assertEquals(true,address.getIsDeleted());
        }
    }
}