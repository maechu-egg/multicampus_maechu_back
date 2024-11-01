package com.multipjt.multi_pjt.user.domain.mypage;

import lombok.Data;

@Data
public class ProfileResponseDTO {
    private int profile_id;
    private String profile_gender; // 성별
    private int profile_age; // 나이
    private String profile_region; //사는 지역
    private float profile_weight; //몸무게
    private float profile_height; //키
    private String profile_goal; //운동목표
    private int member_id; // 외래키
    private String profile_allergy; //알러지
    private String profile_sport1; //운동종목
    private String profile_sport2;
    private String profile_sport3;
    private int profile_workout_frequency; //운동빈도

}
