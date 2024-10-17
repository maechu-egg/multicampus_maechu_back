package com.multipjt.multi_pjt.record.diet.domain;

import lombok.Data;

@Data
public class ItemResponseDTO {
 
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private Integer carbs;
    private Integer protein;
    private Integer fat;
    private Integer calories;
    private Long dietId;    

}
