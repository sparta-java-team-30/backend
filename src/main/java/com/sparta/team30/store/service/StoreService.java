package com.sparta.team30.store.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.respository.CategoryRepository;
import com.sparta.team30.category.exception.CategoryNotFoundException;
import com.sparta.team30.category.service.CategoryService;
import com.sparta.team30.common.exception.DuplicateStoreException;
import com.sparta.team30.common.exception.StoreNotFoundException;
import com.sparta.team30.store.exception.*;
import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.*;
import com.sparta.team30.store.repository.StoreRepository;
import com.sparta.team30.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    public Page<StoreListResponseDto> getStores(
            int page,
            int limit,
            String sortBy,
            String order,
            String search,
            UUID categoryId) {
        if (limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Store> storeList = storeRepository.getStores(pageable, sortBy, order, search, categoryId);
        return storeList.map(StoreListResponseDto::new);
    }

    public StoreResponseDto getStore(UUID storeId) {
        return new StoreResponseDto(storeFindById(storeId));
    }

    public StoreResponseDto getMyStore(User user) {
        Store store = storeRepository.getMyStore(user);
        if(store == null) {
            throw new UserHasNoStoreException("이 사용자는 아직 음식점을 등록하지 않았습니다.");
        }
        return new StoreResponseDto(store);
    }

    public Page<StoreListResponseDto> getUnapprovedStores(int page, int limit, String sortBy, String order) {
        if (limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Store> storeList = storeRepository.findUnapprovedStores(pageable, sortBy, order);
        return storeList.map(StoreListResponseDto::new);
    }

    @Transactional
    public StoreCreateResponseDto createStore(@Valid StoreRequestDto requestDto, User owner) {
        boolean isAlreadyOwner = storeRepository.isAlreadyOwner(owner);

        if (isAlreadyOwner) {
            throw new StoreOwnerAlreadyExistsException("이미 가게가 존재하는 사용자입니다.");
        }

        boolean isDuplicated = storeRepository.isDuplicateStore(
                requestDto.getCategoryId(),
                requestDto.getStoreName(),
                requestDto.getStorePhone(),
                requestDto.getStorePostcode(),
                requestDto.getStoreAddress1()
        );

        if (isDuplicated) {
            throw new DuplicateStoreException("중복된 음식점이 이미 존재합니다.");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() ->
                new CategoryNotFoundException("카테고리를 찾을 수 없습니다.")
        );

        Store store = storeRepository.save(new Store(category, requestDto, owner));
        return new StoreCreateResponseDto(store);
    }

    @Transactional
    public StoreApproveResponseDto approveStore(UUID storeId) {
        Store store = storeFindById(storeId);

        if(store.getIsApproved())
            throw new StoreAlreadyApproveException("이미 승인한 가게입니다.");

        store.setIsApproved(true);

        return new StoreApproveResponseDto(store);
    }

    public boolean isOwner(UUID storeId, UserDetailsImpl userDetails) {
        boolean isOwner = storeRepository.isOwner(storeId, userDetails.getUser());
        if (!isOwner) {
            throw new NotStoreOwnerException("이 사용자는 이 가게의 주인이 아닙니다.");
        }

        return isOwner;
    }

    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto) {
        Store store = storeFindById(storeId);

        if(!store.getIsApproved())
            throw new StoreNotApproveException("승인되지 않은 음식점입니다.");

        if (requestDto.getCategoryId() != null) {
            categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() ->
                    new CategoryNotFoundException("카테고리를 찾을 수 없습니다.")
            );
        }

        long count = storeRepository.updateStore(storeId, requestDto);

        if (count == 0) {
            throw new StoreUpdateFailException("음식점 수정 중 문제가 생겼습니다.");
        }

        return new StoreResponseDto(store);
    }

    @Transactional
    public StoreDeleteResponseDto deleteStore(UUID storeId, String deletedBy) {
        Store store = storeFindById(storeId);

        if(store.getIsDeleted()) {
            throw new StoreAlreadyDeleteException("이미 삭제한 가게입니다.");
        }

        store.delete(deletedBy);
        return new StoreDeleteResponseDto(store);

    }

    public Store storeFindById(UUID storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("해당 음식점을 찾을 수 없습니다.")
        );
    }
}