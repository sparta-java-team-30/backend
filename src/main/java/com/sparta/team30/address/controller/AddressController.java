package com.sparta.team30.address.controller;

import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.dto.RequestUpdateAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDTO;
import com.sparta.team30.address.dto.ResponseAddressDetailsDTO;
import com.sparta.team30.address.service.AddressService;
import com.sparta.team30.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Tag(name = "Address API", description = "주소 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    @Operation(summary = "배송지 목록 조회", description = "배송지 목록을 조회합니다.")

    @ApiResponse(responseCode = "200", description = "배송지 목록 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<List<ResponseAddressDTO>> getAllAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        List<ResponseAddressDTO> list = addressService.getAllAddress(userDetails);
        return ResponseEntity.ok(list);
    }
    @Operation(summary = "배송지 상세 조회", description = "배송지 상세 조회입니다.")
    @ApiResponse(responseCode = "200", description = "주소 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주소",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{address-id}")
    public ResponseEntity<ResponseAddressDetailsDTO> getAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("address-id") UUID addressId) {
        ResponseAddressDetailsDTO addressDetails = addressService.getAddressDetails(userDetails, addressId);

        return ResponseEntity.ok(addressDetails);
    }
    //기본 배송지로 설정
    @Operation(summary = "기본 배송지로 설정", description = "기본 배송지로 설정합니다.")

    @ApiResponse(responseCode = "200", description = "기본 배송지로 설정 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"기본 배송지로 설정되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "사용자 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{address-id}/default")
    public ResponseEntity<Map<String,String>> setDefaultAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("address-id") UUID addressId) {

        addressService.setDefaultAddress(userDetails, addressId);

        return ResponseEntity.ok(Map.of("message", "기본 배송지로 설정되었습니다."));
    }

    @Operation(summary = "배송지 추가", description = "새로운 배송지를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 생성 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"배송지 생성이 완료되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "사용자 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Map<String,String>> addAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody @Valid RequestCreateAddressDTO requestCreateAddressDTO) {
        addressService.addAddress(userDetails, requestCreateAddressDTO);

        return ResponseEntity.ok(Map.of("message", "배송지 생성이 완료되었습니다."));
    }

    @Operation(summary = "배송지 수정", description = "배송지 주소를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 수정 완료",
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(
                    name="200 OK",
                    value = "{\"message\" : \"배송지 수정이 완료되었습니다.\"}"
            )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "406", description = "잘못된 접근입니다",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{address-id}")
    public ResponseEntity<Map<String,String>> editAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable("address-id") UUID addressId,
                                                          @RequestBody @Valid RequestUpdateAddressDTO requestUpdateAddressDTO) {

        addressService.updateAddress(userDetails, addressId, requestUpdateAddressDTO);

        return ResponseEntity.ok(Map.of("message", "배송지 수정이 완료되었습니다."));
    }

    @Operation(summary = "배송지 삭제", description = "등록된 배송지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 삭제 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"배송지 삭제가 완료되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "406", description = "잘못된 접근입니다",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{address-id}")
    public ResponseEntity<Map<String,String>> deleteAddress(@AuthenticationPrincipal UserDetails userDetails,
                                                            @PathVariable("address-id") UUID addressId) {

        addressService.deleteAddress(userDetails, addressId);

        return ResponseEntity.ok(Map.of("message", "배송지 삭제가 완료되었습니다."));
    }
}
