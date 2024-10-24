package com.multipjt.multi_pjt.user.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDTO {
    private String grantType; // jwt인증타입 : Bearer
    private String accessToken;
    private String refreshToken;
}
