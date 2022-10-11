package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.domain.RefreshToken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByMember(Member member);
}
