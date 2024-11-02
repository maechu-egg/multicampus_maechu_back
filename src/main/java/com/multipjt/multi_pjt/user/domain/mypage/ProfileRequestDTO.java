package com.multipjt.multi_pjt.user.domain.mypage;

import lombok.Data;

@Data
public class ProfileRequestDTO {
    //private int profile_id; //auto_increment
    private String profile_gender; // 성별
    private int profile_age; // 나이
    private String profile_region; //사는 지역
    private double profile_weight; //몸무게
    private double profile_height; //키
    private String profile_goal; //운동목표
    private int member_id; // 외래키 - 토큰에서 추출하게 됌
    private String profile_allergy; //알러지 선택사항
    private String profile_sport1; //운동종목 필수사항
    private String profile_sport2; // 종목 선택사항
    private String profile_sport3; // 종목 선택사항
    private int profile_workout_frequency; //운동빈도

    public String getProfile_gender() {
        return profile_gender;
    }

    public void setProfile_gender(String profile_gender) {
        this.profile_gender = profile_gender;
    }

    public int getProfile_age() {
        return profile_age;
    }

    public void setProfile_age(int profile_age) {
        this.profile_age = profile_age;
    }

    public String getProfile_region() {
        return profile_region;
    }

    public void setProfile_region(String profile_region) {
        this.profile_region = profile_region;
    }

    public double getProfile_weight() {
        return profile_weight;
    }

    public void setProfile_weight(double profile_weight) {
        this.profile_weight = profile_weight;
    }

    public double getProfile_height() {
        return profile_height;
    }

    public void setProfile_height(double profile_height) {
        this.profile_height = profile_height;
    }

    public String getProfile_goal() {
        return profile_goal;
    }

    public void setProfile_goal(String profile_goal) {
        this.profile_goal = profile_goal;
    }

    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int member_id) {
        this.member_id = member_id;
    }

    public String getProfile_allergy() {
        return profile_allergy;
    }

    public void setProfile_allergy(String profile_allergy) {
        this.profile_allergy = profile_allergy;
    }

    public String getProfile_sport1() {
        return profile_sport1;
    }

    public void setProfile_sport1(String profile_sport1) {
        this.profile_sport1 = profile_sport1;
    }

    public String getProfile_sport2() {
        return profile_sport2;
    }

    public void setProfile_sport2(String profile_sport2) {
        this.profile_sport2 = profile_sport2;
    }

    public String getProfile_sport3() {
        return profile_sport3;
    }

    public void setProfile_sport3(String profile_sport3) {
        this.profile_sport3 = profile_sport3;
    }

    public int getProfile_workout_frequency() {
        return profile_workout_frequency;
    }

    public void setProfile_workout_frequency(int profile_workout_frequency) {
        this.profile_workout_frequency = profile_workout_frequency;
    }
}
