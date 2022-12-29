package io.clroot.boilerplate.user.dto;

import lombok.Builder;

public record UserRegisterDto(String username,
                              String password,
                              String name,
                              String email,
                              String phoneNumber) {
    @Builder
    public UserRegisterDto {
    }
}
