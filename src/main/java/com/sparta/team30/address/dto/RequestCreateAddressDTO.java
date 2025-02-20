package com.sparta.team30.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateAddressDTO {
    @Schema(description = "우편번호", defaultValue = "12345")
    private String userPostcode;
    @Schema(description = "상세주소 1", defaultValue = "서울특별시 광화문 1")
    private String userAddress1;
    @Schema(description = "상세주소 2", defaultValue = "101호")
    private String userAddress2;
}
