package com.sparta.team30.common.external.dto;

import java.util.List;

public record ExternalApiRequestDto(
        List<Content> contents
) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
}