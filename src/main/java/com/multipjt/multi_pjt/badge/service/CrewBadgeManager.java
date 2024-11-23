package com.multipjt.multi_pjt.badge.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.CrewBadgeMapper;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;

@Service
public class CrewBadgeManager {

    private final CrewBadgeMapper crewBadgeMapper;
    private ConcurrentHashMap<Integer, Boolean> processedMembers = new ConcurrentHashMap<>(); // 이미 처리된 회원 ID 저장
    private static final Logger logger = LoggerFactory.getLogger(CrewBadgeManager.class);

    @Autowired
    public CrewBadgeManager(CrewBadgeMapper crewBadgeMapper) {
        this.crewBadgeMapper = crewBadgeMapper;
    }

    // 배틀 승리 시 뱃지 처리 메서드
    public void processBattleWin(int memberId) {
        try {
            // 크루 멤버 테이블에서 배틀 승리 수 조회
            Integer crewBattleWins = crewBadgeMapper.selectTotalBattleWinsByMemberId(memberId);
            if (crewBattleWins == null) {
                logger.warn("Crew battle wins for member {} is null. Setting to 0.", memberId);
                crewBattleWins = 0; // 기본값 설정
            }
            
            // 크루 뱃지 정보 조회
            CrewBadgeResponseDTO badgeInfo = crewBadgeMapper.selectCrewBadgeByMemberId(memberId);

            // 뱃지 정보가 없으면 새로 생성
            if (badgeInfo == null) {
                CrewBadgeRequestDTO newBadge = new CrewBadgeRequestDTO();
                newBadge.setMember_id(memberId);
                newBadge.setCrew_current_points(crewBattleWins); // 초기 점수는 배틀 승리 수로 설정
                newBadge.setBadge_level(determineBadgeLevel(crewBattleWins));
                newBadge.setCrew_battle_wins(crewBattleWins); // 배틀 승리 수 설정
                crewBadgeMapper.insertBadge(newBadge);
            } else {
                // 뱃지 정보가 있으면 업데이트
                int previousBattleWins = badgeInfo.getCrew_battle_wins(); // 이전 배틀 승리 수

                // 크루 멤버 테이블의 배틀 승리 수와 비교
                if (crewBattleWins > previousBattleWins) {
                    int difference = crewBattleWins - previousBattleWins; // 차이 계산

                    // 점수 업데이트
                    float updatedPoints = badgeInfo.getCrew_current_points() + difference; // 차이만큼 점수 추가
                    badgeInfo.setCrew_current_points(updatedPoints);
                    badgeInfo.setBadge_level(determineBadgeLevel(updatedPoints));
                    badgeInfo.setCrew_battle_wins(crewBattleWins); // 크루 뱃지 테이블의 배틀 승리 수 업데이트

                    CrewBadgeRequestDTO updateBadge = new CrewBadgeRequestDTO();
                    updateBadge.setCrew_badge_id(badgeInfo.getCrew_badge_id());
                    updateBadge.setCrew_current_points(updatedPoints);
                    updateBadge.setMember_id(badgeInfo.getMember_id());
                    updateBadge.setBadge_level(badgeInfo.getBadge_level());
                    updateBadge.setCrew_battle_wins(crewBattleWins); // 크루 배틀 승리 수 업데이트 추가

                    // 크루 뱃지 테이블 업데이트
                    crewBadgeMapper.updateBadge(updateBadge);

                    // 배틀 승수 업데이트 호출
                    crewBadgeMapper.updateCrewBattleWins(memberId, crewBattleWins); // 추가
                } else if (crewBattleWins < previousBattleWins) {
                    // 만약 배틀 승리 수가 줄어들 경우에도 업데이트
                    crewBadgeMapper.updateCrewBattleWins(memberId, crewBattleWins); // 크루 배틀 승리 수 업데이트
                }
            }
        } catch (Exception e) {
            logger.error("Error processing battle win for member {}: {}", memberId, e.getMessage());
        }
    }

    // 새로운 배틀 승리 수를 측정하는 메서드
    // private int measureNewBattleWins(int memberId) {
    //     Integer newWins = crewBadgeMapper.selectNewBattleWinsByMemberId(memberId);
    //     logger.info("New Battle Wins for member {}: {}", memberId, newWins); // 로그 추가
    //     return (newWins != null && newWins > 0) ? newWins : 0; // null일 경우 0 반환
    // }

    // 점수에 따른 뱃지 등급 결정 메서드
    private String determineBadgeLevel(float points) {
        if (points >= 100) {
            return "다이아";
        } else if (points >= 70) {
            return "플래티넘";
        } else if (points >= 50) {
            return "골드";
        } else if (points >= 30) {
            return "실버";
        } else if (points >= 10) {
            return "브론즈";
        } else {
            return "기본";
        }
    }

    public CrewBadgeResponseDTO selectCrewBadgeByMemberId(int memberId) {
        return crewBadgeMapper.selectCrewBadgeByMemberId(memberId); // 뱃지 정보를 조회하여 반환
    }

    // 크루 뱃지 업데이트 메서드 추가
    public void updateBadge(CrewBadgeRequestDTO badgeRequest) {
        crewBadgeMapper.updateBadge(badgeRequest); // 뱃지 정보를 업데이트
    }

    // 특정 회원의 배틀 승수를 조회하는 메서드 추가
    public Integer getBattleWins(int memberId) {
        return crewBadgeMapper.selectTotalBattleWinsByMemberId(memberId);
    }

    // 배틀 승수 업데이트 메서드 추가
    public void updateCrewBattleWins(int memberId) {
        // 배틀 승수를 업데이트하는 쿼리 호출
        crewBadgeMapper.updateCrewBattleWins(memberId, getBattleWins(memberId)); // 수정된 부분
    }

    // 크루 뱃지 랭킹 조회 메서드
    public List<Map<String, Object>> getCrewBadgeRanking() {
        return crewBadgeMapper.selectCrewBadgeRanking();
    }
    // 크루 뱃지 생성
    public void createCrewBadge(int memberId) {
        try {
            // 크루 뱃지 정보가 이미 존재하는지 확인
            CrewBadgeResponseDTO existingBadge = crewBadgeMapper.selectCrewBadgeByMemberId(memberId);
            
            if (existingBadge != null) {
                logger.warn("Crew badge for member {} already exists. No new badge created.", memberId);
                return; // 이미 존재하는 경우, 새로운 뱃지를 생성하지 않음
            }
    
            // 새로운 뱃지 생성
            CrewBadgeRequestDTO newBadge = new CrewBadgeRequestDTO();
            newBadge.setMember_id(memberId);
            newBadge.setCrew_current_points(0); // 초기 점수는 0으로 설정
            newBadge.setBadge_level("기본"); // 기본 뱃지 레벨 설정
            newBadge.setCrew_battle_wins(0); // 초기 배틀 승리 수는 0으로 설정
    
            // 뱃지 정보를 데이터베이스에 삽입
            crewBadgeMapper.insertBadge(newBadge);
            logger.info("New crew badge created for member {}.", memberId);
        } catch (Exception e) {
            logger.error("Error creating crew badge for member {}: {}", memberId, e.getMessage());
        }
    }
}
