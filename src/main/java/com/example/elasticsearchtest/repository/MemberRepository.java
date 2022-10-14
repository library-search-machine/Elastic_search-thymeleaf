package com.example.elasticsearchtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.elasticsearchtest.domain.Member;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByNickName(String id);
}
