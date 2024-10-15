package com.multipjt.multi_pjt.user.domain;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String member_id;
    private String member_img;
    private String member_nickname;
    private String member_password;
    private String member_email;
    private String member_phone;
    private String member_type; //Oauth 2.0 사용시 어떤 sns인지
}
