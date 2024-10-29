package com.multipjt.multi_pjt.badge.domain.record;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserActivityRecordResponseDTO {
    private int recordId;
    private String activityType;
    private float points;
    private LocalDateTime createdDate;
    private int memberId;
}
