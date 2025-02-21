package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderProductDTO {
    private UUID productId;
    @Schema(description = "상품명", defaultValue = "햄버거")
    private String productName;
    @Schema(description = "수량", defaultValue = "2")
    private int quantity;

}

