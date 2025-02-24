package com.sparta.team30.category.service;

import static org.mockito.Mockito.*;
import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.*;
import com.sparta.team30.category.respository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void testSuccessGetCategories() {
        List<Category> categoryList = Arrays
                .asList(new Category(new CategoryRequestDto("한식")),
                        new Category(new CategoryRequestDto("중식")));
        when(categoryRepository.getCategories()).thenReturn(categoryList);

        List<CategoryListResponseDto> categoryListResponseDtoList = categoryService.getCategories();
        assertNotNull(categoryListResponseDtoList);
        assertEquals("한식", categoryListResponseDtoList.get(0).getCategoryName());
        assertEquals("중식", categoryListResponseDtoList.get(1).getCategoryName());
    }

    @Test
    void testSuccessCreateCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("한식");
        Category category = new Category(categoryRequestDto);

        System.out.println(categoryRepository.getClass());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryCreateResponseDto responseDto = categoryService.createCategory(categoryRequestDto);
        assertNotNull(responseDto);
        assertEquals("한식", responseDto.getCategoryName());
    }

    @Test
    void testSuccessUpdateCategory() {
        UUID categoryId = UUID.randomUUID();
        CategoryRequestDto requestDto = new CategoryRequestDto("한식");
        Category category = new Category(new CategoryRequestDto("중식"));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDto responseDto = categoryService.updateCategory(categoryId, requestDto);
        assertNotNull(responseDto);
        assertEquals("한식", responseDto.getCategoryName());
    }

    @Test
    void testSuccessDeleteCategory() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category(new CategoryRequestDto("한식"));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDeleteResponseDto responseDto = categoryService.deleteCategory(categoryId, "tempUser");
        assertNotNull(responseDto);
        assertEquals(true, responseDto.getIsDeleted());
    }

    @Test
    void testSuccessCategoryFindById() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category(new CategoryRequestDto("한식"));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category categoryResult = categoryService.categoryFindById(categoryId);
        assertEquals("한식", categoryResult.getCategoryName());
    }

    @Test
    void testSuccessIsDuplicated() {
        String categoryName = "한식";

        when(categoryRepository.isCategoryNameExists(categoryName)).thenReturn(false);

        boolean result = categoryService.isDuplicated(categoryName);

        assertFalse(result);
    }

}