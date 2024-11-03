package com.multipjt.multi_pjt.record.diet.domain;

import lombok.Data;

@Data
public class ItemResponseDTO {
 
    private Integer item_id;
    private String item_name;
    private Integer quantity;
    private Integer carbs;
    private Integer protein;
    private Integer fat;
    private Integer calories;
    private Integer diet_id;    

}
