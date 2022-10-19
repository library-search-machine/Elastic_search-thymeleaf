package com.example.elasticsearchtest.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
@RedisHash(value = "member", timeToLive = 3600)
public class RefreshToken {
    @Id
    private Long id;
    private String nickname;

    private String token;
    public RefreshToken(Member member, String refreshToken) {
        id = member.getId();
        nickname = member.getNickName();
        token = refreshToken;
    }
}
