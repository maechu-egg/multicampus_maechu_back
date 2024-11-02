package com.multipjt.multi_pjt.user.domain.login;

import lombok.Data;

@Data
public class UserResponseDTO {
   

    private int member_id;         // 회원 ID (PK 디비에는 member_id로 저장됨)
    private String member_img;     // 프로필 이미지 URL(이미지 파일의 이름)
    private String nickname;       // 닉네임
    private String password;
    private String email;          // 이메일
    private String phone;          // 전화번호
    private boolean verified;      // 인증 여부
    private String snsProvider;    // SNS 제공자 (KAKAO, NAVER 등)
    private String regDate;
    

    // Getter and Setter for member_id
    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }

    // Getter and Setter for member_img
    public String getMemberImg() {
        return member_img;
    }

    public void setMemberImg(String member_img) {
        this.member_img = member_img;
    }

    // Getter and Setter for nickname
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for verified
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    // Getter and Setter for snsProvider
    public String getSnsProvider() {
        return snsProvider;
    }

    public void setSnsProvider(String snsProvider) {
        this.snsProvider = snsProvider;
    }

    // Getter and Setter for regDate
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    
}
