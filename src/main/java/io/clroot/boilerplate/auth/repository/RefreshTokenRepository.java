package io.clroot.boilerplate.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.clroot.boilerplate.auth.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByPayload(String payload);

    void deleteByPayload(String payload);
}
