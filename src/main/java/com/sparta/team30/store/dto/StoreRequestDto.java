package com.sparta.team30.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "음식점 등록 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {

    @NotNull(message = "카테고리는 필수입니다.")
    private UUID categoryId;

    @NotBlank(message = "음식점 이름은 필수입니다.")
    private String storeName;

    @NotBlank(message = "음식점 전화번호는 필수입니다.")
    private String storePhone;

    @NotBlank(message = "음식점 우편번호는 필수입니다.")
    private String storePostcode;

    @NotBlank(message = "음식점 주소는 필수입니다.")
    private String storeAddress1;

    private String storeAddress2;
}
