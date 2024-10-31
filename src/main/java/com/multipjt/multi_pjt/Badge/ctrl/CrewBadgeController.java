package com.multipjt.multi_pjt.badge.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.service.CrewBadgeManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/crew_badges")
public class CrewBadgeController {
    private static final Logger logger = LoggerFactory.getLogger(CrewBadgeController.class);

    private final CrewBadgeManager crewBadgeManager;

    public CrewBadgeController(CrewBadgeManager crewBadgeManager) {
        this.crewBadgeManager = crewBadgeManager;
    }

    // 크루 뱃지 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCrewBadge(@Valid @RequestBody CrewBadgeRequestDTO badgeRequest) {
        try {
            logger.info("Creating crew badge for member: {}", badgeRequest.getMember_id());
            // 배틀 승리 수를 포함하여 뱃지 생성
            crewBadgeManager.processBattleWin(badgeRequest.getMember_id());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "생성 성공");
            response.put("message", "크루 뱃지가 생성되었습니다.");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for crew badge creation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating crew badge", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "크루 뱃지 생성 중 오류가 발생했습니다."));
        }
    }

    // 배틀 승수를 조회하여 점수를 크루 뱃지에 반영하는 API
    @PostMapping("/{memberId}/updatePoints")
    public ResponseEntity<Map<String, Object>> updateCrewBadgePoints(@PathVariable("memberId") int memberId) {
        try {
            logger.info("Updating crew badge points and battle wins for member: {}", memberId);
            
            // 배틀 승수 기반으로 점수 업데이트
            crewBadgeManager.processBattleWin(memberId);
            
            // 배틀 승수 조회
            Integer totalBattleWins = crewBadgeManager.getBattleWins(memberId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success"); 
            response.put("message", "크루 뱃지 점수와 배틀 승수가 업데이트되었습니다.");
            response.put("totalBattleWins", totalBattleWins);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for updating crew badge points and battle wins: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating crew badge points and battle wins", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("status", "error", "message", "크루 뱃지 점수와 배틀 승수 업데이트 중 오류가 발생했습니다."));
        }
    }

    // 특정 회원의 크루 뱃지 정보 조회 API 
    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, Object>> getCrewBadgeByMemberId(@PathVariable("memberId") int memberId) {
        try {
            logger.info("Fetching crew badge for member: {}", memberId);
            CrewBadgeResponseDTO badgeInfo = crewBadgeManager.selectCrewBadgeByMemberId(memberId);
            
            if (badgeInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "error", "message", "크루 뱃지를 찾을 수 없습니다."));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("badgeInfo", badgeInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching crew badge for member: {}", memberId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "크루 뱃지 조회 중 오류가 발생했습니다."));
        }
    }

    // 전역 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
    }
}
