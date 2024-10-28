package com.multipjt.multi_pjt.record.openApi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutirientDTO {

    // 식품군
    @JsonProperty("GROUP_NAME")
    private String foodClass;

    // 식품명
    @JsonProperty("DESC_KOR")
    private String foodNm;
    
    // 칼로리
    @JsonProperty("NUTR_CONT1")
    private String energy;
    
    // 양
    @JsonProperty("SERVING_SIZE")
    private String quantity;
    
    // 탄수화물
    @JsonProperty("NUTR_CONT2")
    private String carbs;

    // 단백질
    @JsonProperty("NUTR_CONT3")
    private String protein;
    
    // 지방
    @JsonProperty("NUTR_CONT4")
    private String fat;

    // 당
    @JsonProperty("NUTR_CONT5")
    private String sugar;

    // 나트륨
    @JsonProperty("NUTR_CONT6")
    private String nat;
    
    // 콜르스테롤
    @JsonProperty("NUTR_CONT7")
    private String chole;

    // 포화지방
    @JsonProperty("NUTR_CONT8")
    private String fatsat;
    
    // 트랜스지방
    @JsonProperty("NUTR_CONT9")
    private String fatrn;
}
