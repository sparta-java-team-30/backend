package com.sparta.team30.address.dto;

import com.sparta.team30.address.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResponseAddressDetailsDTO {

    private UUID addressId;
    @Schema(description = "우편번호", example = "12345")
    private String userPostcode;

    private String userAddress1;
    private String userAddress2;
    private Boolean isDeleted;
    private String nickname;
    private Boolean isDefault;
    private LocalDateTime updatedAt;


}
