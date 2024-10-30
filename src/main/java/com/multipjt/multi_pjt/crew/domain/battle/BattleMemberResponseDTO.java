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
                badgeImagePath = "img/crewBadge/CrewBadgeDefault.png";
            } else {
                switch (badge_level) {
                    case "기본":
                        badgeImagePath = "img/crewBadge/CrewBadgeDefault.png";
                        break;
                    case "브론즈":
                        badgeImagePath = "img/crewBadge/CrewBadgeBronze.png";
                        break;
                    case "실버":
                        badgeImagePath = "img/crewBadge/CrewBadgeSilver.png";
                        break;
                    case "골드":
                        badgeImagePath = "img/crewBadge/CrewBadgeGold.png";
                        break;
                    case "플래티넘":
                        badgeImagePath = "img/crewBadge/CrewBadgePlatinum.png";
                        break;
                    case "다이아":
                        badgeImagePath = "img/crewBadge/CrewBadgeDiamond.png";
                        break;
                    default:
                        badgeImagePath = "img/crewBadge/CrewBadgeDefault.png";
                }
            }
        }
        return badgeImagePath;
    }
}
