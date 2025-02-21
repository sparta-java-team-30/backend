package com.sparta.team30.common.external.dto;

import java.util.List;

public record ExternalApiResponseDto(
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