package io.clroot.boilerplate.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.clroot.boilerplate.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
