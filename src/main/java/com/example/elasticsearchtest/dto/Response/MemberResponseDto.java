package com.example.elasticsearchtest.dto.Response;

import com.example.elasticsearchtest.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private boolean ok;
    private String message;
    private String Authorization;
    private String RefreshToken;

    public MemberResponseDto(boolean ok, String message) {
        this.ok = ok;
        this.message =message;
    }

    public MemberResponseDto(TokenDto tokenDto) {
        this.ok = true;
        this.message ="로그인 성공";
        this.Authorization =tokenDto.getAccessToken();
        this.RefreshToken = tokenDto.getRefreshToken();

    }
}
