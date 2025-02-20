package com.sparta.team30.store.controller;

import com.sparta.team30.store.dto.*;
import com.sparta.team30.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Store API", description = "음식점 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "음식점 조회", description = "음식점 전체 조회, 음식점 이름으로 조회, 음식점 카테고리별 조회합니다.")
    @ApiResponse(responseCode = "200", description = "음식점 조회 성공")
    @GetMapping("/stores")
    public Page<StoreListResponseDto> getStores(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 음식점 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt, updatedAt)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @Parameter(description = "음식점 이름 조회 키워드")
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @Parameter(description = "카테고리별 조회 키워드")
            @RequestParam(value = "category", required = false, defaultValue = "") UUID categoryId
    ) {
        return storeService.getStores(page-1, limit, sortBy, order, search, categoryId);
    }

    @Operation(summary = "특정 음식점 조회", description = "UUID를 이용해서 특정 음식점을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 음식점 조회 성공"),
            @ApiResponse(responseCode = "404", description = "음식점을 찾을 수 없음")
    })
    @GetMapping("/stores/{store-id}")
    public ResponseEntity<StoreResponseDto> getStore(
            @Parameter(description = "특정 음식점 UUID")
            @PathVariable("store-id") UUID uuid
    ) {
        StoreResponseDto response = storeService.getStore(uuid);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "승인되지 않은 음식점 조회", description = "음식점 등록 후 아직 승인되지 않은 음식점을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "음식점 조회 성공")
    @GetMapping("/stores/unapproved")
    public Page<StoreListResponseDto> getUnapprovedStores(
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @Parameter(description = "보여줄 음식점 개수")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @Parameter(description = "정렬 기준 필드 (createdAt: 생성일순, updatedAt: 수정일순)")
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "정렬 방식 (asc: 오름차순, desc: 내림차순)")
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order
    ) {
        return storeService.getUnapprovedStores(page-1, limit, sortBy, order);
    }

    @Operation(summary = "음식점 등록", description = "새로운 음식점을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음식점 등록 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 조회 실패"),
            @ApiResponse(responseCode = "409", description = "음식점 등록 실패"),
    })
    @PostMapping("/stores")
    public ResponseEntity<StoreCreateResponseDto> createStore (@RequestBody @Valid StoreRequestDto requestDto) {
        StoreCreateResponseDto response = storeService.createStore(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "음식점 승인", description = "승인되지 않은 음식점을 승인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음식점 승인 성공"),
            @ApiResponse(responseCode = "404", description = "음식점을 찾을 수 없음")
    })
    @PatchMapping("/stores/{store-id}")
//    @Secured({"ROLE_MANAGER", "ROLE_MASTER"})
    public ResponseEntity<StoreApproveResponseDto> approveStore(
            @Parameter(description = "승인할 음식점 UUID")
            @PathVariable("store-id") UUID uuid
    ) {
        StoreApproveResponseDto response = storeService.approveStore(uuid);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "음식점 수정", description = "음식점의 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음식점 수정 성공"),
            @ApiResponse(responseCode = "404", description = "음식점 수정 실패")
    })
    @PutMapping("/stores/{store-id}")
//    @Secured({"ROLE_OWNER", "ROLE_MANAGER", "ROLE_MASTER"})
    public ResponseEntity<StoreResponseDto> updateStore(
            @Parameter(description = "수정할 음식점 UUID")
            @PathVariable("store-id") UUID uuid,
            @Parameter(description = "수정할 음식점 데이터")
            @RequestBody @Valid StoreUpdateRequestDto requestDto
    ) {
        StoreResponseDto response = storeService.updateStore(uuid, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "음식점 삭제", description = "음식점을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "음식점 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "음식점을 찾을 수 없음")
    })
    @DeleteMapping("/stores/{store-id}")
//    @Secured({"ROLE_OWNER", "ROLE_MANAGER", "ROLE_MASTER"})
    public ResponseEntity<StoreDeleteResponseDto> deleteStore(
            @Parameter(description = "삭제할 음식점 UUID")
            @PathVariable("store-id") UUID uuid
    ) {
        StoreDeleteResponseDto response = storeService.deleteStore(uuid, "tempUser");
        return ResponseEntity.ok(response);
    }
}
