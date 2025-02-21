package com.sparta.team30.carts.controller;

import com.sparta.team30.carts.dto.CartItemRequestDto;
import com.sparta.team30.carts.dto.CartItemResponseDto;
import com.sparta.team30.carts.service.CartService;
import com.sparta.team30.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Tag(name = "Cart API", description = "장바구니 관련 API")
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 아이템 조회", description = "사용자의 장바구니 아이템 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 아이템 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Page<CartItemResponseDto>> getCartItems(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam("page") int page,
            @Parameter(description = "한 페이지의 크기", example = "10") @RequestParam("size") int size,
            @Parameter(description = "정렬 기준 필드", example = "productName") @RequestParam("sortBy") String sortBy,
            @Parameter(description = "오름차순 여부 (true: 오름차순, false: 내림차순)", example = "true") @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID cartId = cartService.getCartId(userDetails.getUser().getId());

        if (cartId == null) {
            cartId = cartService.createCart(userDetails.getUser().getId());
        }

        Page<CartItemResponseDto> cartItemsResponseDto = cartService.getCartItems(cartId, page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok(cartItemsResponseDto);
    }

    @Operation(summary = "장바구니 아이템 추가", description = "사용자의 장바구니에 상품을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 아이템 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResponseDto> addItemToCart(
            @RequestBody CartItemRequestDto cartItem,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartItemResponseDto cartItemsResponseDto = cartService.addItemToCart(userDetails.getUser().getId(), cartItem);
        return ResponseEntity.ok(cartItemsResponseDto);
    }

    @Operation(summary = "장바구니 아이템 수정", description = "사용자의 장바구니에서 특정 아이템의 수량을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 아이템 수정 성공"),
            @ApiResponse(responseCode = "404", description = "장바구니 아이템을 찾을 수 없음")
    })
    @PatchMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItem(
            @Parameter(description = "장바구니 아이템 아이디", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID cartItemId,
            @Parameter(description = "수정할 수량", example = "3") @RequestParam int count,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CartItemResponseDto cartItemsResponseDto = cartService.updateCartItem(userDetails.getUser().getId(), cartItemId, count);
        return ResponseEntity.ok(cartItemsResponseDto);
    }

    @Operation(summary = "장바구니 아이템 삭제", description = "사용자의 장바구니에서 특정 아이템을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "장바구니 아이템 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "장바구니 아이템을 찾을 수 없음")
    })
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "장바구니 아이템 아이디", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID cartItemId
    ) {
        cartService.deleteCartItem(userDetails.getUser().getId(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}