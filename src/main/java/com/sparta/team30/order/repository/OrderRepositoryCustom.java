package com.sparta.team30.order.repository;

import com.sparta.team30.order.dto.ResponseOrderHistoryDTO;
import com.sparta.team30.user.domain.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom{

    Page<ResponseOrderHistoryDTO> findByUserIdAndProductOrStoreName(String search, Long userId, UserRoleEnum role, String username, Pageable pageable, boolean isAsc);
}
