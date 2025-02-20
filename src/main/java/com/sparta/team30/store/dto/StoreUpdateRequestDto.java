package com.sparta.team30.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "음식점 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequestDto {

    private UUID categoryId;
    private String storeName;
    private String storePhone;
    private String storePostcode;
    private String storeAddress1;
    private String storeAddress2;

}