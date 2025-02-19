package com.sparta.team30.products.controller;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductRequestDto;
import com.sparta.team30.products.dto.ProductResponseDto;
import com.sparta.team30.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Product API", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 생성 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(productRequestDto);
        return ResponseEntity.ok(productResponseDto);
    }

    @Operation(summary = "전체 상품 조회", description = "페이징 처리된 상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 완료"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam("page") int page,
            @Parameter(description = "한 페이지의 크기", example = "10") @RequestParam("size") int size,
            @Parameter(description = "정렬 기준 필드", example = "productName") @RequestParam("sortBy") String sortBy,
            @Parameter(description = "오름차순 여부 (true: 오름차순, false: 내림차순)", example = "true") @RequestParam("isAsc") boolean isAsc
    ) {
        Page<ProductResponseDto> productResponseDto = productService.getAllProducts(page - 1, size, sortBy, isAsc);

        if (productResponseDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }

        return ResponseEntity.ok(productResponseDto);
    }

    @Operation(summary = "상품 상세 조회", description = "상품 아이디로 특정 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "상품 아이디", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID productId) {
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return productResponseDto != null ? ResponseEntity.ok(productResponseDto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "상품 수정", description = "상품 아이디로 특정 상품 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Parameter(description = "상품 아이디", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID productId,
            @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(productId, productRequestDto);
        return productResponseDto != null ? ResponseEntity.ok(productResponseDto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "상품 삭제", description = "상품 아이디로 특정 상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "상품 아이디", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID productId) {
        boolean isDeleted = productService.deleteProduct(productId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
