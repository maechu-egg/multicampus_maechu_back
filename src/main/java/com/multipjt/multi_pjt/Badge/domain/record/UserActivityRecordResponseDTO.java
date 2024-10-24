package com.multipjt.multi_pjt.badge.domain.record;

import lombok.Data;

@Data
public class UserActivityRecordResponseDTO {
    private int record_id;
    private String activity_type; // enum을 문자열로 처리
    private float points;
    private String created_date;
    private int member_id;


    
}
