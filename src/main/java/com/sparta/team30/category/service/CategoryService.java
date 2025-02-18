package com.sparta.team30.category.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.CategoryResponseDto;
import com.sparta.team30.category.respository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
