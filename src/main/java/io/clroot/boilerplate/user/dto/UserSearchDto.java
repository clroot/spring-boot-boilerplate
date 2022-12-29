package io.clroot.boilerplate.user.dto;

import io.clroot.boilerplate.common.dto.SearchDto;
import lombok.Builder;

@Builder
public record UserSearchDto(Long id,
                            String username,
                            String name,
                            String email,
                            String phoneNumber) implements SearchDto {
}
