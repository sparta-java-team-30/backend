package com.sparta.team30.store.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.CategoryDto;
import com.sparta.team30.category.service.CategoryService;
import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.*;
import com.sparta.team30.store.repository.StoreRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private StoreService storeService;

    @Test
    void testSuccessGetStores() {
        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        String search = "한식";
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Store> storeList = new PageImpl<>(List.of(store));

        when(storeRepository.getStores(pageable, sortBy, order, search, category.getCategoryId())).thenReturn(storeList);

        Page<StoreListResponseDto> StoreListResponseDtos =
                storeService.getStores(page, limit, sortBy, order, search, category.getCategoryId());

        assertEquals(0, StoreListResponseDtos.getNumber());
    }

    @Test
    void testSuccessGetStore() {
        UUID storeId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);
        store.setStoreName("한식당1");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        StoreResponseDto responseDto = storeService.getStore(storeId);

        assertNotNull(responseDto);
        assertEquals("한식당1", responseDto.getStoreName());
    }

    @Test
    void testSuccessGetMyStore() {
        User user = new User(
                "sample@gmail.com",
                "user1",
                "1234",
                "user1",
                Boolean.TRUE,
                UserRoleEnum.USER
        );
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);
        store.setStoreName("한식당1");

        when(storeRepository.getMyStore(user)).thenReturn(store);

        StoreResponseDto responseDto = storeService.getMyStore(user);

        assertNotNull(responseDto);
        assertEquals("한식당1", responseDto.getStoreName());
    }

    @Test
    void testSuccessGetUnapprovedStores() {
        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Store> storeList = new PageImpl<>(List.of(store));

        when(storeRepository.findUnapprovedStores(pageable, sortBy, order)).thenReturn(storeList);

        Page<StoreListResponseDto> StoreListResponseDtos =
                storeService.getUnapprovedStores(page, limit, sortBy, order);

        assertEquals(0, StoreListResponseDtos.getNumber());
    }

    @Test
    void testSuccessCreateStore() {
        User user = new User(
                "sample@gmail.com",
                "user1",
                "1234",
                "user1",
                Boolean.TRUE,
                UserRoleEnum.USER
        );
        StoreRequestDto requestDto = new StoreRequestDto();
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        requestDto.setCategoryId(category.getCategoryId());
        Store store = new Store();
        store.setCategory(category);

        when(storeRepository.isAlreadyOwner(user)).thenReturn(false);
        when(storeRepository.isDuplicateStore(any(), any(), any(), any(), any())).thenReturn(false);
        when(categoryService.getCategoryById(requestDto.getCategoryId())).thenReturn(new CategoryDto(category));
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        StoreCreateResponseDto responseDto = storeService.createStore(requestDto, user);

        assertNotNull(responseDto);
    }

    @Test
    void testSuccessApproveStore() {
        UUID storeId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        StoreApproveResponseDto responseDto = storeService.approveStore(storeId);

        assertNotNull(responseDto);
        assertTrue(responseDto.getIsApproved());
    }

    @Test
    void testSuccessIsOwner() {
        UUID storeId = UUID.randomUUID();
        User user = new User(
                "sample@gmail.com",
                "user1",
                "1234",
                "user1",
                Boolean.TRUE,
                UserRoleEnum.USER
        );
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(storeRepository.isOwner(storeId, user)).thenReturn(true);

        boolean isOwner = storeService.isOwner(storeId, userDetails);

        assertTrue(isOwner);
    }

    @Test
    void testSuccessUpdateStore() {
        UUID storeId = UUID.randomUUID();
        StoreUpdateRequestDto requestDto = new StoreUpdateRequestDto();
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setIsApproved(true);
        store.setCategory(category);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.updateStore(storeId, requestDto)).thenReturn(1L);

        StoreResponseDto responseDto = storeService.updateStore(storeId, requestDto);

        assertNotNull(responseDto);
    }

    @Test
    void testSuccessDeleteStore() {
        UUID storeId = UUID.randomUUID();
        String deletedBy = "admin";
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        StoreDeleteResponseDto responseDto = storeService.deleteStore(storeId, deletedBy);

        assertNotNull(responseDto);
        assertTrue(responseDto.getIsDeleted());
        assertEquals("admin", store.getDeletedBy());
    }

    @Test
    void testSuccessStoreFindById() {
        UUID storeId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(UUID.randomUUID());
        Store store = new Store();
        store.setCategory(category);
        store.setStoreName("한식당1");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        Store store1 = storeService.storeFindById(storeId);

        assertNotNull(store1);
        assertEquals("한식당1", store1.getStoreName());
    }
}