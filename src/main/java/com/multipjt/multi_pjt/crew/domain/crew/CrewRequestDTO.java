package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewRequestDTO {
    private int crew_id;
    private String crew_name;
    private String crew_goal;
    private String crew_title;
    private String crew_location;
    private String crew_sport;
    private String crew_gender;
    private String crew_frequency;
    private String crew_age;
    private String crew_date;
    private int member_id;
}
