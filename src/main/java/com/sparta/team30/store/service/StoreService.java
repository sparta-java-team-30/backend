package com.sparta.team30.store.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.respository.CategoryRepository;
import com.sparta.team30.category.exception.CategoryNotFoundException;
import com.sparta.team30.common.exception.DuplicateStoreException;
import com.sparta.team30.common.exception.StoreNotFoundException;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.*;
import com.sparta.team30.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;


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

    public StoreResponseDto getStore(UUID uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(() ->
                new StoreNotFoundException(messageSource.getMessage(
                        "nod.found.store",
                        null,
                        "Not Found Store",
                        Locale.getDefault()
                ))
        );
        return new StoreResponseDto(store);
    }

    public Page<StoreListResponseDto> getUnapprovedStores(int page, int limit, String sortBy, String order) {
        if (limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Sort.Direction direction = ("asc").equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Store> storeList = storeRepository.findUnapprovedStores(pageable, sortBy, order);
        return storeList.map(StoreListResponseDto::new);
    }

    @Transactional
    public StoreCreateResponseDto createStore(@Valid StoreRequestDto requestDto) {
        boolean isDuplicated = storeRepository.isDuplicateStore(
                requestDto.getCategoryId(),
                requestDto.getStoreName(),
                requestDto.getStorePhone(),
                requestDto.getStorePostcode(),
                requestDto.getStoreAddress1()
        );
        if (isDuplicated) {
            throw new DuplicateStoreException(messageSource.getMessage(
                    "duplicate.store",
                    null,
                    "Duplicate Store",
                    Locale.getDefault()
            ));
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() ->
                new CategoryNotFoundException(messageSource.getMessage(
                        "not.found.category",
                        null,
                        "Not Found Category",
                        Locale.getDefault()
                ))
        );
        Store store = storeRepository.save(new Store(category, requestDto));
        return new StoreCreateResponseDto(store);
    }

    @Transactional
    public StoreApproveResponseDto approveStore(UUID uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(() ->
                new StoreNotFoundException(messageSource.getMessage(
                        "nod.found.store",
                        null,
                        "Not Found Store",
                        Locale.getDefault()
                ))
        );
        store.approve();
        return new StoreApproveResponseDto(store);
    }

    @Transactional
    public StoreResponseDto updateStore(UUID uuid, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(uuid).orElseThrow(() ->
                new StoreNotFoundException(messageSource.getMessage(
                        "not.found.store",
                        null,
                        "Not Found Store",
                        Locale.getDefault()
                ))
        );
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() ->
                    new CategoryNotFoundException(messageSource.getMessage(
                            "not.found.category",
                            null,
                            "Not Found Category",
                            Locale.getDefault()
                    ))
            );
            store.setCategory(category);
        }
        if (requestDto.getStoreName() != null && !requestDto.getStoreName().isEmpty()) {
            store.setStoreName(requestDto.getStoreName());
        }
        if (requestDto.getStorePhone() != null && !requestDto.getStorePhone().isEmpty()) {
            store.setStorePhone(requestDto.getStorePhone());
        }
        if (
                (requestDto.getStorePostcode() != null && !requestDto.getStorePostcode().isEmpty())
                        &&
                        (requestDto.getStoreAddress1() != null && !requestDto.getStoreAddress1().isEmpty())
        ) {
            store.setStorePostcode(requestDto.getStorePostcode());
            store.setStoreAddress1(requestDto.getStoreAddress1());
            if (requestDto.getStoreAddress2() != null && !requestDto.getStoreAddress2().isEmpty()) {
                store.setStoreAddress2(requestDto.getStoreAddress2());
            }
        }
        return new StoreResponseDto(store);
    }

    @Transactional
    public StoreDeleteResponseDto deleteStore(UUID uuid, String deletedBy) {
        Store store = storeRepository.findById(uuid).orElseThrow(() ->
                new StoreNotFoundException(messageSource.getMessage(
                        "not.found.store",
                        null,
                        "Not Found Store",
                        Locale.getDefault()
                ))
        );
        store.delete(deletedBy);
        return new StoreDeleteResponseDto(store);
    }
}