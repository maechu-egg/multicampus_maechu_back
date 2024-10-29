package com.multipjt.multi_pjt.badge.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.service.CrewBadgeManager;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> createCrewBadge(@RequestBody CrewBadgeRequestDTO badgeRequest) {
        try {
            logger.info("Creating crew badge for member: {}", badgeRequest.getMember_id());
            crewBadgeManager.processBattleWin(badgeRequest.getMember_id());
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "크루 뱃지가 생성되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for crew badge creation: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error creating crew badge", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "크루 뱃지 생성 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 특정 크루원의 뱃지 정보 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, Object>> getCrewBadge(@PathVariable int memberId) {
        try {
            logger.info("Fetching crew badge for member: {}", memberId);
            CrewBadgeResponseDTO badge = crewBadgeManager.selectCrewBadgeByMemberId(memberId);
            
            if (badge == null) {
                logger.info("No badge found for member: {}", memberId);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "not_found");
                response.put("message", "해당 회원의 크루 뱃지를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", badge);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching crew badge", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "크루 뱃지 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 크루 뱃지 업데이트
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCrewBadge(@RequestBody CrewBadgeRequestDTO badgeRequest) {
        try {
            logger.info("Updating crew badge for member: {}", badgeRequest.getMember_id());
            crewBadgeManager.updateBadge(badgeRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "크루 뱃지가 업데이트되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for crew badge update: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            logger.error("Error updating crew badge", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "크루 뱃지 업데이트 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // 전역 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
    }
}
