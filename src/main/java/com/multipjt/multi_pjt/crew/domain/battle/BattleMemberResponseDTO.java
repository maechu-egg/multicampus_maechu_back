package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;

@Data
public class BattleMemberResponseDTO {
    private Integer participant_id;
    private Integer battle_id;
    private Integer member_id;
    private String  nickname;
    private String  profile_region;
    private Integer profile_age;
    private Integer total_feed_kcal;
    private Integer total_feed_exTime;
    private Integer feed_count;
    private String badge_level;

    public String getBadgeImagePath() {
        switch (badge_level) {
            case "기본":
                return "img/badges/crewBadgeBasic.png"; // 기본 배지 이미지
            case "브론즈":
                return "img/badges/crewBadgeBronze.png";
            case "실버":
                return "img/badges/crewBadgeSilver.png";
            case "골드":
                return "img/badges/crewBadgeGold.png";
            case "플래티넘":
                return "img/badges/crewBadgePlatinum.png";
            case "다이아":
                return "img/badges/crewBadgeDiamond.png";
            default:
                return null; // 기본값 추가
        }
    }
}
