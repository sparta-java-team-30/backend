package com.sparta.team30.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RequestUpdateAddressDTO {
    @Schema(description = "우편번호", defaultValue = "98765")
    private String userPostcode;
    @Schema(description = "상세주소 1", defaultValue = "서울특별시 광화문 999")
    private String userAddress1;
    @Schema(description = "상세주소 2", defaultValue = "1901호")
    private String userAddress2;
}
