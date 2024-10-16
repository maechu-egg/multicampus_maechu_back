package com.multipjt.multi_pjt.user.domain.mypage;

import lombok.Data;

@Data
public class ProfileRequestDTO {
    private String profile_id;
    private String profile_gender;
    private int profile_age;
    private String profile_region;
    private float profile_weight;
    private float profile_height;
    private String profile_goal;
    private String profile_time;
    private String member_id; // 외래키
    private String profile_allergy;
    private String profile_diet_goal;
    private String profile_sport1;
    private String profile_sport2;
    private String profile_sport3;
    private int profile_workout_frequency;

}
