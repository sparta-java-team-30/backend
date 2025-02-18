package com.sparta.team30.user.repository;

import com.sparta.team30.user.domain.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    void updateCreatedBy(String createdBy, Long id);
}
