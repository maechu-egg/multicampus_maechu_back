package com.multipjt.multi_pjt.user.domain;

import lombok.Data;

@Data
public class UserResponseDTO {
    private int member_id;         // 회원 ID (PK)
    private String member_img;        // 프로필 이미지 URL
    private String nickname;         // 닉네임
    private String password;
    private String email;            // 이메일
    private String phone;            // 전화번호
    private boolean verified;        // 인증 여부
    private String snsProvider;      // SNS 제공자 (KAKAO, NAVER 등)
    private String regDate;
}
