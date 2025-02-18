package com.sparta.team30.category.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.CategoryRequestDto;
import com.sparta.team30.category.dto.CategoryResponseDto;
import com.sparta.team30.category.respository.CategoryRepository;
import com.sparta.team30.common.exception.CategoryNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    public List<CategoryResponseDto> getCategories() {
        List<Category> categoryList = categoryRepository.findByIsDeletedFalse();
        List<CategoryResponseDto> categoryResponseDtoList = new ArrayList<>();

        for (Category c : categoryList) {
            categoryResponseDtoList.add(new CategoryResponseDto(c));
        }
        return categoryResponseDtoList;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = categoryRepository.save(new Category(requestDto));
        return new CategoryResponseDto(category);
    }

    @Transactional
    public CategoryResponseDto updateCategory(UUID uuid, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(uuid).orElseThrow(() ->
                new CategoryNotFoundException(messageSource.getMessage(
                        "not.found.category",
                        null,
                        "Not Found Category",
                        Locale.getDefault()
                ))
        );
        category.update(requestDto);
        return new CategoryResponseDto(category);
    }
}
