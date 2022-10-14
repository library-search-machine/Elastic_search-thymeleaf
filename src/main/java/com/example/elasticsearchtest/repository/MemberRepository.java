package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);
}
