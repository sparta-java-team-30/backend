package com.sparta.team30.category.controller;

import com.sparta.team30.category.dto.CategoryRequestDto;
import com.sparta.team30.category.dto.CategoryResponseDto;
import com.sparta.team30.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> response = categoryService.getCategories();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/categories")
//    @Secured({"ROLE_MANAGER", "ROLE_MASTER"})
    public ResponseEntity<CategoryResponseDto> createCategory (@RequestBody @Valid CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{uuid}")
//    @Secured({"ROLE_MANAGER", "ROLE_MASTER"})
    public ResponseEntity<CategoryResponseDto> updateCategory (@PathVariable UUID uuid, @RequestBody @Valid CategoryRequestDto requestDto) {
        CategoryResponseDto response = categoryService.updateCategory(uuid, requestDto);
        return ResponseEntity.ok(response);
    }


}
