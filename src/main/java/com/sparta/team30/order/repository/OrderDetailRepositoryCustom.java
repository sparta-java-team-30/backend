package com.sparta.team30.order.repository;


import com.sparta.team30.order.dto.ResponseOrderProductDTO;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepositoryCustom {

    List<ResponseOrderProductDTO> findByOrderId(UUID orderId);

    String findStoreNameByOrderId(UUID orderId);
}
