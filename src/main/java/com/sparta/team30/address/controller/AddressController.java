package com.sparta.team30.address.controller;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.service.AddressService;
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
    public ResponseEntity<ResponseAddressDetailsDTO> getAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("{address-id}") UUID addressId) {
        ResponseAddressDetailsDTO addressDetails = addressService.getAddressDetails(userDetails, addressId);

        return ResponseEntity.ok(addressDetails);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> addAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RequestCreateAddressDTO requestCreateAddressDTO) {

        addressService.addAddress(userDetails, requestCreateAddressDTO);

        return ResponseEntity.ok(Map.of("message", "배송지 생성이 완료되었습니다."));
    }
}
