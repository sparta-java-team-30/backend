package com.sparta.team30.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateAddressDTO {
    private String userPostcode;
    private String userAddress1;
    private String userAddress2;
}
