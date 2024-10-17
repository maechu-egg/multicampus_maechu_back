package com.multipjt.multi_pjt.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
   
    private String member_img;
    private String nickname;         // 닉네임
    private String password;         // 비밀번호 (일반 회원일 경우 사용)
    private String email;            // 이메일
    private String phone;            // 전화번호
    private boolean verified;
    private String snsProvider;      // SNS 제공자 (예: KAKAO, NAVER)
   
}
