package com.multipjt.multi_pjt.record.diet.domain;

import lombok.Data;

@Data
public class ItemRequestDTO {
 
    //private Long itemId;  autoIncrement
    private String itemName;
    private Integer quantity;
    private Integer carbs;
    private Integer protein;
    private Integer fat;
    private Integer calories;
    private Long dietId;    

}
