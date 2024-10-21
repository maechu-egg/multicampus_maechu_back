package com.multipjt.multi_pjt.user.domain;

import lombok.Data;

@Data
public class LoginDTO {
    private String memberEmail;
    private String memeberPassword;

    public LoginDTO() {

    }
    
    public LoginDTO(String email, String pw) {
        this.memberEmail = email;
        this.memeberPassword = pw;
    }

    public LoginDTO(UserRequestDTO userRequestDTO) {
        this.memberEmail = userRequestDTO.getEmail();
        this.memeberPassword = userRequestDTO.getPassword();
    }

}
