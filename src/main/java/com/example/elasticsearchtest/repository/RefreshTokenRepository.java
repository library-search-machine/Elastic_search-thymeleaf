package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
