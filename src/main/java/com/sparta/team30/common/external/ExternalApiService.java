package com.sparta.team30.common.external;

import com.sparta.team30.common.external.dto.ExternalApiRequestDto;
import com.sparta.team30.common.external.dto.ExternalApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalApiService {
    @Value("${gemini.uri}")
    private String requestUri;
    @Value("${gemini.key}")
    private String requestKey;
    private static final String PROMPT_SUFFIX = " 상품의 설명을 50자 이내로 추천해줘";

    private final RestClient restClient;

    public String requestProductDetailRecommand(String productName) {

        ExternalApiRequestDto request = new ExternalApiRequestDto(
                List.of(new ExternalApiRequestDto.Content(
                        List.of(new ExternalApiRequestDto.Part(productName + PROMPT_SUFFIX))
                ))
        );

        URI uri = UriComponentsBuilder
                .fromUriString(requestUri + "?key=" + requestKey)
                .encode()
                .build()
                .toUri();

        ExternalApiResponseDto response = restClient.post()
                .uri(uri)
                .body(request)
                .retrieve()
                .onStatus(
                        status -> status.getStatusCode().is4xxClientError() || status.getStatusCode().is5xxServerError()
                ).body(ExternalApiResponseDto.class);

        String result = extractResponseText(response);
        if (result == null){
            throw new RuntimeException("API 요청 실패");
        }

        return result;
    }

    private String extractResponseText(ExternalApiResponseDto response) {
        return response.candidates().get(0)
                .content().parts().get(0)
                .text();
    }
}
