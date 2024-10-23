package com.multipjt.multi_pjt.user.domain;

import lombok.Data;

@Data
public class LoginDTO {
    private String password;
    private String email;   

    public LoginDTO() {

    }
    
    public LoginDTO(String email, String pw) {
        this.email = email;
        this.password = pw;
    }

    public LoginDTO(UserRequestDTO userRequestDTO) {
        this.email = userRequestDTO.getEmail();
        this.password = userRequestDTO.getPassword();
    }

}
