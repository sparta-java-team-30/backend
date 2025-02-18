package com.sparta.team30.order.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestUpdateOrderDTO {
    private String comment;
    private UUID addressId;
}
