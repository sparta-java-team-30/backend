package com.sparta.team30.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "음식점 등록 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {

    @Schema(description = "음식점 카테고리", example = "e2eea1ab-463e-42e7-82d5-0da14235fe5a")
    @NotNull(message = "카테고리는 필수입니다.")
    private UUID categoryId;

    @Schema(description = "음식점 이름", example = "한식당1")
    @NotBlank(message = "음식점 이름은 필수입니다.")
    private String storeName;

    @Schema(description = "음식점 전화번호", example = "0211111111")
    @NotBlank(message = "음식점 전화번호는 필수입니다.")
    private String storePhone;

    @Schema(description = "음식점 우편번호", example = "01234")
    @NotBlank(message = "음식점 우편번호는 필수입니다.")
    private String storePostcode;

    @Schema(description = "음식점 주소", example = "서울시 중구")
    @NotBlank(message = "음식점 주소는 필수입니다.")
    private String storeAddress1;

    @Schema(description = "음식점 상세주소", example = "1층")
    private String storeAddress2;
}
