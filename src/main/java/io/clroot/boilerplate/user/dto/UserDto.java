package io.clroot.boilerplate.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import io.clroot.boilerplate.user.model.User;
import io.clroot.boilerplate.user.model.UserRole;
import lombok.Builder;

/**
 * A DTO for the {@link User} entity
 */
public record UserDto(Long id,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String username,
                      String name,
                      String email,
                      String phoneNumber,
                      Set<UserRole> roles)
    implements Serializable {

    @Builder
    public UserDto {
    }

    public static UserDto of(User entity) {
        return UserDto.builder()
            .id(entity.getId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .username(entity.getUsername())
            .name(entity.getName())
            .email(entity.getEmail())
            .phoneNumber(entity.getPhoneNumber())
            .roles(entity.getRoles())
            .build();
    }
}
