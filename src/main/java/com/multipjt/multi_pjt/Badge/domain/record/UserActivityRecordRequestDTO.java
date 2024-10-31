package com.multipjt.multi_pjt.badge.domain.record;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserActivityRecordRequestDTO {
    private Integer recordId;
    private String activityType;
    private LocalDateTime createdDate;
    private int memberId;

    // 추가: 운동 기록의 날짜를 비교하기 위한 필드
    private LocalDateTime activityDate;
}
