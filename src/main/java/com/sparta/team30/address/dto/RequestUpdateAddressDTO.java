package com.sparta.team30.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestUpdateAddressDTO {
    @Schema(description = "우편번호", defaultValue = "98765")
    @NotNull(message = "우편번호를 입력해주세요")
    private String userPostcode;
    @NotNull(message = "주소를 입력해주세요")
    @Schema(description = "상세주소 1", defaultValue = "서울특별시 광화문 999")
    private String userAddress1;
    @Schema(description = "상세주소 2", defaultValue = "1901호")
    private String userAddress2;
}
