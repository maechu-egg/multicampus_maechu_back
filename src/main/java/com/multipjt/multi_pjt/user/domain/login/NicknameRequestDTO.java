package com.multipjt.multi_pjt.user.domain.login;

import lombok.Data;

@Data
public class NicknameRequestDTO {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
}
