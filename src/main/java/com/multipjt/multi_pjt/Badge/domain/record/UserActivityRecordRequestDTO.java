package com.multipjt.multi_pjt.badge.domain.record;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserActivityRecordRequestDTO {
    private Integer record_id;
    private String activity_type;
    private float points;
    private LocalDateTime created_date;
    private int member_id;
}
