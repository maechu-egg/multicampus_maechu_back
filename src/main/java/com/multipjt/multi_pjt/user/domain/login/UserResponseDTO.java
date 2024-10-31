package com.multipjt.multi_pjt.user.domain.login;

import lombok.Data;

@Data
public class UserResponseDTO {
    private int memberId;          // 회원 ID
    private String memberImg;      // 프로필 이미지 파일 이름
    private String nickname;       // 닉네임
    private String email;          // 이메일
    private String phone;          // 전화번호
    private boolean verified;      // 인증 여부
    private String snsProvider;    // SNS 제공자 (KAKAO, NAVER 등)
    private String regDate;        // 등록일
    private String memberImgUrl;   // 이미지 URL
    private String password;

    
}
