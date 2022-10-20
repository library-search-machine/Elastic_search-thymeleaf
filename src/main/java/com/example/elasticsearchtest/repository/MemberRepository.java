package com.example.elasticsearchtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.elasticsearchtest.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface MemberRepository extends MongoRepository<Member,String> {
    Optional<Member> findByNickName(String id);
}
