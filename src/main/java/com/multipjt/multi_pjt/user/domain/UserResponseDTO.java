package com.multipjt.multi_pjt.user.domain;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String member_id;
    private String member_img;
    private String member_nickname;
    private String member_password;
    private String member_email;
    private String member_phone;
    private String member_type;
}
