package com.multipjt.multi_pjt.user.domain.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCertificationInputDTO {
    // 사용자가 이메일 인증코드를 입력하는 DTO 
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "인증코드는 필수 항목입니다. ")
    private String certificationCode;
}
