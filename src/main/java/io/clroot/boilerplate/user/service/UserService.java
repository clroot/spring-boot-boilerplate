package io.clroot.boilerplate.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.clroot.boilerplate.user.dto.UserDto;
import io.clroot.boilerplate.user.dto.UserRegisterDto;
import io.clroot.boilerplate.user.dto.UserSearchDto;
import io.clroot.boilerplate.user.exception.UsernameConflictException;
import io.clroot.boilerplate.user.model.User;
import io.clroot.boilerplate.user.model.UserRole;
import io.clroot.boilerplate.user.repository.UserRepository;
import io.clroot.boilerplate.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    public Page<UserDto> search(UserSearchDto searchDTO, Pageable pageable) {
        return userSearchRepository.search(searchDTO, pageable)
            .map(UserDto::of);
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        return UserDto.of(user);
    }

    @Transactional
    public UserDto register(UserRegisterDto registerDTO) {
        checkDuplicateUsername(registerDTO.username());

        User newUser = User.builder()
            .username(registerDTO.username())
            .password(passwordEncoder.encode(registerDTO.password()))
            .name(registerDTO.name())
            .email(registerDTO.email())
            .phoneNumber(registerDTO.phoneNumber())
            .build();
        newUser.addRole(UserRole.USER);

        userRepository.save(newUser);

        return UserDto.of(newUser);
    }

    private void checkDuplicateUsername(String username) {
        userRepository.findByUsername(username)
            .ifPresent(user -> {
                throw new UsernameConflictException("Username already exists: " + username);
            });
    }
}
