package com.sparta.team30.store.repository;

import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.StoreUpdateRequestDto;
import com.sparta.team30.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StoreRepositoryCustom {

    boolean isDuplicateStore (
            UUID categoryId,
            String storeName,
            String storePhone,
            String storePostcode,
            String storeAddress1
    );
    Page<Store> getStores(Pageable pageable, String sortBy, String order, String search, UUID categoryId);
    Page<Store> findUnapprovedStores(Pageable pageable, String sortBy, String order);
    Store getMyStore(User user);
    boolean isOwner(UUID storeId, User user);
    boolean isAlreadyOwner(User user);
    long updateStore(UUID storeId, StoreUpdateRequestDto requestDto);

}