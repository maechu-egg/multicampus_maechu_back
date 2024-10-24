package com.multipjt.multi_pjt.user.domain.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCertificationRequestDTO {
//이메일 인증 요청 시 사용하는 DTO 
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;
}
