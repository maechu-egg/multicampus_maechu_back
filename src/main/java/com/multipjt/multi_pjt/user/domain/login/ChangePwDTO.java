package com.multipjt.multi_pjt.user.domain.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePwDTO {
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    private String password;
}
