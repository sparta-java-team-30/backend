package com.sparta.team30.address.dto;

import com.sparta.team30.address.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResponseAddressDetailsDTO {

    private UUID addressId;
    private String userPostcode;
    private String userAddress1;
    private String userAddress2;
    private Boolean isDeleted;
    private String nickname;
    private Boolean isDefault;
    private LocalDateTime updatedAt;


}
