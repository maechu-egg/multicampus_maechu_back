package com.multipjt.multi_pjt.user.domain.login;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private int member_id;
    private String nickname;
    private String phone;
    private String member_img; 
    
    public int getMemberId() {
        return member_id;
    }
    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhoneNumber() { return phone; }
    public void setPhoneNumber(String phone) { this.phone = phone; }

    public String getMemberImg() { return member_img; }
    public void setMemberImg(String member_img) { 
        this.member_img = member_img; }
}
