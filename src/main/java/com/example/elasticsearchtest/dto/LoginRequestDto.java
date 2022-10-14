package com.example.elasticsearchtest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;
}
