package com.multipjt.multi_pjt.badge.ctrl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

import com.multipjt.multi_pjt.badge.service.IBadgeService;
import com.multipjt.multi_pjt.badge.service.MemberBadgeManager;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;

@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final IBadgeService badgeService;
    private final MemberBadgeManager memberBadgeManager;

    // BadgeController 생성자
    public BadgeController(IBadgeService badgeService, MemberBadgeManager memberBadgeManager) {
        this.badgeService = badgeService;
        this.memberBadgeManager = memberBadgeManager;
    }

    // 활동을 처리하고 포인트를 업데이트하는 엔드포인트
    @PostMapping("/processActivity")
    public ResponseEntity<Map<String, Object>> processActivity(@RequestBody UserActivityRecordRequestDTO request) {
        try {
            // 활동을 처리하고 포인트를 업데이트
            memberBadgeManager.processActivity(request.getMember_id(), request.getActivity_type());
            BigDecimal currentPoints = badgeService.getCurrentPoints(Long.valueOf(request.getMember_id()));
            String badgeLevel = badgeService.getBadgeLevel(currentPoints);

            // 성공 응답
            return ResponseEntity.ok(Map.of(
                "message", String.format("활동 처리 완료: %s, 회원 ID: %d", request.getActivity_type(), request.getMember_id()),
                "currentPoints", currentPoints,
                "badgeLevel", badgeLevel
            ));
        } catch (IllegalArgumentException e) {
            // 잘못된 요청에 대한 응답 반환
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // 예기치 않은 오류에 대한 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
        }
    }

    // 1. 사용자 점수 및 등급 조회
    @GetMapping("/user/{memberId}/status")
    public ResponseEntity<Map<String, Object>> getUserBadgeStatus(@PathVariable("memberId") Long memberId) {
        BigDecimal currentPoints = badgeService.getCurrentPoints(memberId);
        String badgeLevel = badgeService.getBadgeLevel(currentPoints);
        BigDecimal pointsToNextBadge = badgeService.getPointsToNextBadge(currentPoints);
        
        return ResponseEntity.ok(Map.of(
            "currentPoints", currentPoints,
            "badgeLevel", badgeLevel,
            "pointsToNextBadge", pointsToNextBadge
        ));
    }

    // 2. 사용자 점수 업데이트
    @PutMapping("/user/{memberId}/updatePoints")
    public ResponseEntity<String> updateUserPoints(@PathVariable("memberId") Long memberId, @RequestBody BigDecimal points) {
        badgeService.updateUserPoints(memberId, points);
        return ResponseEntity.ok("점수가 업데이트되었습니다.");
    }

    // 3. 사용자 점수 순위 통계
    @GetMapping("/user/rankings")
    public ResponseEntity<List<Map<String, Object>>> getUserRankings() {
        List<Map<String, Object>> rankings = badgeService.getUserRankings();
        return ResponseEntity.ok(rankings);
    }

    // 전역 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
    }

    // 회원의 모든 활동 처리
    @PostMapping("/user/{memberId}/activities")
    public ResponseEntity<String> processUserActivities(@PathVariable("memberId") Long memberId) {
        try {
            badgeService.processUserActivities(memberId);
            return ResponseEntity.ok("회원의 모든 활동이 처리되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("활동 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
