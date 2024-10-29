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
    private String badgeImagePath;
    

    public String getBadgeImagePath() {
        if (badgeImagePath == null) {
            if (badge_level == null) {
                badgeImagePath = "img/badges/CrewBadgeDefault.png";
            } else {
                switch (badge_level) {
                    case "기본":
                        badgeImagePath = "img/badges/CrewBadgeDefault.png";
                        break;
                    case "브론즈":
                        badgeImagePath = "img/badges/CrewBadgeBronze.png";
                        break;
                    case "실버":
                        badgeImagePath = "img/badges/CrewBadgeSilver.png";
                        break;
                    case "골드":
                        badgeImagePath = "img/badges/CrewBadgeGold.png";
                        break;
                    case "플래티넘":
                        badgeImagePath = "img/badges/CrewBadgePlatinum.png";
                        break;
                    case "다이아":
                        badgeImagePath = "img/badges/CrewBadgeDiamond.png";
                        break;
                    default:
                        badgeImagePath = "img/badges/CrewBadgeDefault.png";
                }
            }
        }
        return badgeImagePath;
    }
}
