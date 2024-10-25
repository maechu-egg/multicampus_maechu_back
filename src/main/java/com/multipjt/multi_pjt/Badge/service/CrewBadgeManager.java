package com.multipjt.multi_pjt.badge.service;

import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.CrewBadgeMapper;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;

@Service
public class CrewBadgeManager {

    private final CrewBadgeMapper crewBadgeMapper;

    public CrewBadgeManager(CrewBadgeMapper crewBadgeMapper) {
        this.crewBadgeMapper = crewBadgeMapper;
    }

    // 배틀 승리 시 뱃지 처리 메서드
    public void processBattleWin(int memberId) {
        // 1. 특정 회원의 배틀 승수 조회
        int battleWins = crewBadgeMapper.selectBattleWinsByMemberId(memberId);

        if (battleWins <= 0) {
            throw new IllegalArgumentException("No battle wins found for memberId: " + memberId);
        }

        // 2. 특정 회원의 크루 뱃지 정보 조회
        CrewBadgeResponseDTO badgeInfo = crewBadgeMapper.selectCrewBadgeByMemberId(memberId);

        // 3. 크루원의 뱃지 정보가 없는 경우 새로 생성
        if (badgeInfo == null) {
            CrewBadgeRequestDTO newBadge = new CrewBadgeRequestDTO();
            newBadge.setMember_id(memberId);
            newBadge.setCrew_current_points(battleWins);  // 배틀 승수만큼 점수 부여
            newBadge.setBadge_level(determineBadgeLevel(battleWins));
            crewBadgeMapper.insertBadge(newBadge);
        } else {
            // 4. 기존 뱃지 정보 업데이트
            float updatedPoints = badgeInfo.getCrew_current_points() + battleWins;  // 배틀 승수만큼 점수 추가
            badgeInfo.setCrew_current_points(updatedPoints);
            badgeInfo.setBadge_level(determineBadgeLevel(updatedPoints));
            
            CrewBadgeRequestDTO updateBadge = new CrewBadgeRequestDTO();
            updateBadge.setCrew_badge_id(badgeInfo.getCrew_badge_id());
            updateBadge.setCrew_current_points(updatedPoints);
            updateBadge.setMember_id(badgeInfo.getMember_id());
            updateBadge.setBadge_level(badgeInfo.getBadge_level());
            
            crewBadgeMapper.updateBadge(updateBadge);
        }
    }

    // 점수에 따른 뱃지 등급 결정 메서드
    private String determineBadgeLevel(float points) {
        if (points >= 100) {
            return "Diamond";
        } else if (points >= 70) {
            return "Platinum";
        } else if (points >= 50) {
            return "Gold";
        } else if (points >= 30) {
            return "Silver";
        } else if (points >= 10) {
            return "Bronze";
        } else {
            return "No Badge";
        }
    }

    public CrewBadgeResponseDTO selectCrewBadgeByMemberId(int memberId) {
        return crewBadgeMapper.selectCrewBadgeByMemberId(memberId); // 뱃지 정보를 조회하여 반환
    }

    // 크루 뱃지 업데이트 메서드 추가
    public void updateBadge(CrewBadgeRequestDTO badgeRequest) {
        crewBadgeMapper.updateBadge(badgeRequest); // 뱃지 정보를 업데이트
    }
}
