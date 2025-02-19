package com.sparta.team30.address.controller;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.RequestUpdateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.service.AddressService;
import com.sparta.team30.address.tdo.RequestUpdateAddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<ResponseAddressDTO>> getAllAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        List<ResponseAddressDTO> list = addressService.getAllAddress(userDetails);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{address-id}")
    public ResponseEntity<ResponseAddressDetailsDTO> getAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("address-id") UUID addressId) {
        ResponseAddressDetailsDTO addressDetails = addressService.getAddressDetails(userDetails, addressId);

        return ResponseEntity.ok(addressDetails);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> addAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RequestCreateAddressDTO requestCreateAddressDTO) {

        addressService.addAddress(userDetails, requestCreateAddressDTO);

        return ResponseEntity.ok(Map.of("message", "배송지 생성이 완료되었습니다."));
    }

    @PutMapping("/{address-id}")
    public ResponseEntity<Map<String,String>> editAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable("address-id") UUID addressId,
                                                          @RequestBody RequestUpdateAddressDTO requestUpdateAddressDTO) {

        addressService.updateAddress(userDetails, addressId, requestUpdateAddressDTO);

        return ResponseEntity.ok(Map.of("message", "배송지 수정이 완료되었습니다."));
    }

    @DeleteMapping("/{address-id}")
    public ResponseEntity<Map<String,String>> deleteAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                            @PathVariable("address-id") UUID addressId) {

        addressService.deleteAddress(userDetails, addressId);

        return ResponseEntity.ok(Map.of("message", "배송지 삭제가 완료되었습니다."));
    }
}
