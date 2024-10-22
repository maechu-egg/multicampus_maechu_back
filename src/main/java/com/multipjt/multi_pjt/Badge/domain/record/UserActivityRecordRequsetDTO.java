package com.multipjt.multi_pjt.Badge.domain.record;

import lombok.Data;

@Data
public class UserActivityRecordRequsetDTO {
    private int record_id;
    private enum activity_type {
        DIET,
        EXERCISE,
        COMMENT,
        POST
    }
    private float points;
    private String created_date;
    private int member_id;
}
