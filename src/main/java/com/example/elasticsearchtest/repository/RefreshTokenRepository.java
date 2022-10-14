package com.example.elasticsearchtest.repository;


import com.example.elasticsearchtest.domain.RefreshToken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(RefreshToken refreshTokenObject);
}
