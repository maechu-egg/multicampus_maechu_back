package com.multipjt.multi_pjt.record.openApi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutirientDTO {

    // 식품군
    private String foodClass;

    // 식품명
    private String foodNm;

    // 칼로리
    private Double energy; 

    // 탄수화물
    private Double carbs;

    // 단백질
    private Double protein;
    
    // 지방
    private Double fat;

    // 당
    private Double sugar;

    // 나트륨
    private Double nat;
    
    // 콜르스테롤
    private Double chole;

    // 포화지방
    private Double fatsat;
    
    // 트랜스지방
    private Double fatrn;

    // 칼슘
    private Double cal;
}
