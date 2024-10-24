package com.multipjt.multi_pjt.community.domain.UserActivity;

import java.util.Date;

import lombok.Data;

@Data
public class UserActivityRequestDTO {
    
    private int user_activity_id;
    
    // enum 제어는 Controller에서
    private String user_activity;
    
    private Date activity_date;
    
    // 외래키
    private int post_id;
    private int member_id;
}
