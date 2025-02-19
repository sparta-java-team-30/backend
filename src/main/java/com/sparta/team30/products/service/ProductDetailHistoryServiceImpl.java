package com.sparta.team30.products.service;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.domain.ProductDetail;
import com.sparta.team30.products.domain.ProductDetailHistory;
import com.sparta.team30.products.dto.ProductDetailHistoryRequestDto;
import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;
import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.repository.ProductDetailHistoryRepository;
import com.sparta.team30.products.repository.ProductDetailRepository;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDetailHistoryServiceImpl implements ProductDetailHistoryService {
    private final ProductDetailHistoryRepository productDetailHistoryRepository;
    private final ProductDetailService productDetailService;
    private final ProductRepository productRepository;
    private final RestClient restClient;
    private final ProductDetailRepository productDetailRepository;
    @Value("${gemini.uri}")
    private String requestUri;
    @Value("${gemini.key}")
    private String requestKey;
    private static final String PROMPT_SUFFIX = " 상품의 설명을 50자 이내로 추천해줘";

    @Transactional
    @Override
    public ProductDetailHistoryResponseDto generateContent(UUID productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품이 존재하지 않습니다."));

        ProductDetailHistoryRequestDto request = new ProductDetailHistoryRequestDto(
                List.of(new ProductDetailHistoryRequestDto.Content(
                        List.of(new ProductDetailHistoryRequestDto.Part(product.getProductName() + PROMPT_SUFFIX))
                ))
        );

        URI uri = UriComponentsBuilder
                .fromUriString(requestUri + "?key=" + requestKey)
                .encode()
                .build()
                .toUri();

        ProductDetailHistoryResponseDto response = restClient.post()
                .uri(uri)
                .body(request)
                .retrieve()
                .onStatus(
                        status -> status.getStatusCode().is4xxClientError() || status.getStatusCode().is5xxServerError()
                ).body(ProductDetailHistoryResponseDto.class);

        String result = extractResponseText(response);
        if (result == null){
            throw new RuntimeException("API 요청 실패");
        }

        productDetailService.createProductDetail(productId, new ProductDetailRequestDto(result));

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        ProductDetailHistory productDetailHistory = ProductDetailHistory.builder()
                .productDetail(productDetail)
                .productDetailRequest(product.getProductName() + PROMPT_SUFFIX)
                .productDetailResponse(result)
                .build();

        productDetailHistoryRepository.save(productDetailHistory);

        return response;
    }

    private String extractResponseText(ProductDetailHistoryResponseDto response) {
        return response.candidates().get(0)
                .content().parts().get(0)
                .text();
    }
}
