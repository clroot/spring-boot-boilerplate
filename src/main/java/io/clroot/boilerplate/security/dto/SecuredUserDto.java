package io.clroot.boilerplate.security.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.clroot.boilerplate.user.dto.UserDto;
import io.clroot.boilerplate.user.model.User;
import lombok.Getter;

public class SecuredUserDto extends org.springframework.security.core.userdetails.User {
    @Getter
    private final UserDto userDTO;

    public SecuredUserDto(User user) {
        super(
            user.getUsername(),
            user.getPassword(),
            user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .toList()
        );
        this.userDTO = UserDto.of(user);
    }
}
