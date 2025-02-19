package com.sparta.team30.products.dto;

import java.util.List;

public record ProductDetailHistoryResponseDto(
        List<Candidate> candidates
) {
    public record Candidate(
            Content content,
            String finishReason
    ) {}
    public record Content(
            List<Part> parts
    ) {}
    public record Part(String text) {}
}