package io.clroot.boilerplate.common.dto;

import lombok.Builder;

@Builder
public record ErrorDto(int statusCode,
                       String error,
                       String message) {
}
