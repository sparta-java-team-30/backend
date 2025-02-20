package com.sparta.team30.store.repository;

import com.sparta.team30.store.domain.Store;
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
}