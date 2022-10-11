package com.example.elasticsearchtest.domain;

import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


import java.io.Serializable;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.LOGIN_INPUT_INVALID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;



    @Column(nullable = false)
    @JsonIgnore
    private String password;

    public Member(String id, String password, PasswordEncoder passwordEncoder) {
        this.nickName = id;
        this.password = passwordEncoder.encode(password);
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new BusinessException("로그인 실패", LOGIN_INPUT_INVALID);
        }

    }

}
