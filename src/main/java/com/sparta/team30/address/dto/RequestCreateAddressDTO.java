package com.sparta.team30.address.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestCreateAddressDTO {
    private String userPostcode;
    private String userAddress1;
    private String userAddress2;
}
