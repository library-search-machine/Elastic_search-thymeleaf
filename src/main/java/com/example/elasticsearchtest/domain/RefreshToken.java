package com.example.elasticsearchtest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

//import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Entity
@RedisHash
public class RefreshToken {

    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String keyValue;

    public void updateValue(String token) {
        this.keyValue = token;
    }
}
