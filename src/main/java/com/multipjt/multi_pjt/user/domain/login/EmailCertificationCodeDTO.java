package com.multipjt.multi_pjt.user.domain.login;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmailCertificationCodeDTO {
// 이메일과 인증코드를 저장하는 객체 
    private String email;
    private String certificationCode;
    private LocalDateTime expiryTime;  // 만료 시간 추가
}
