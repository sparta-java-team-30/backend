package com.sparta.team30.category.controller;

import com.sparta.team30.category.dto.*;
import com.sparta.team30.category.service.CategoryService;
import com.sparta.team30.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Category API", description = "카테고리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 조회", description = "카테고리 전체 조회")
    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryListResponseDto>> getCategories() {
        List<CategoryListResponseDto> response = categoryService.getCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
            @ApiResponse(responseCode = "409", description = "카테고리 등록 실패")
    })
    @PostMapping("/categories")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CategoryCreateResponseDto> createCategory (
            @RequestBody @Valid CategoryRequestDto requestDto
    ) {
        CategoryCreateResponseDto response = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "409", description = "카테고리 수정 실패")
    })
    @PutMapping("/categories/{category-id}")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDto> updateCategory (
            @Parameter(description = "수정할 카테고리 UUID")
            @PathVariable("category-id") UUID storeId,
            @Parameter(description = "수정할 카테고리 데이터")
            @RequestBody @Valid CategoryRequestDto requestDto
    ) {
        CategoryResponseDto response = categoryService.updateCategory(storeId, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없는 카테고리에 접근"),
            @ApiResponse(responseCode = "410", description = "이미 삭제한 카테고리")
    })
    @DeleteMapping("/categories/{category-id}")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<CategoryDeleteResponseDto> deleteCategory (
            @Parameter(description = "삭제할 카테고리 UUID")
            @PathVariable("category-id") UUID storeId,
            @Parameter(description = "삭제한 유저")
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CategoryDeleteResponseDto response = categoryService.deleteCategory(storeId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
