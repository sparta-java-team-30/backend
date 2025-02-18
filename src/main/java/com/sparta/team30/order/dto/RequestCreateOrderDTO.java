package com.sparta.team30.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCreateOrderDTO {
    private List<RequestOrderProductDTO> productList;
    private String comment;
}
