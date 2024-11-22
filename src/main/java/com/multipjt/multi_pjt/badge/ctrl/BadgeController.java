package com.multipjt.multi_pjt.badge.ctrl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;
import com.multipjt.multi_pjt.badge.service.IBadgeService;
import com.multipjt.multi_pjt.badge.service.MemberBadgeManager;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/badges")
public class BadgeController {

    private static final Logger logger = LoggerFactory.getLogger(BadgeController.class);

    private final IBadgeService badgeService;
    private final MemberBadgeManager memberBadgeManager;
    private final UserActivityRecordMapper userActivityRecordMapper;
    private final MemberBadgeMapper memberBadgeMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // BadgeController 생성자
    public BadgeController(IBadgeService badgeService, MemberBadgeManager memberBadgeManager, 
                           UserActivityRecordMapper userActivityRecordMapper, MemberBadgeMapper memberBadgeMapper) {
        this.badgeService = badgeService;
        this.memberBadgeManager = memberBadgeManager;
        this.userActivityRecordMapper = userActivityRecordMapper;
        this.memberBadgeMapper = memberBadgeMapper;
    }

    // 활동을 처리하고 포인트를 업데이트하는 엔드포인트
    @PostMapping("/processActivity")
    public ResponseEntity<Map<String, Object>> processActivity(@RequestBody UserActivityRecordRequestDTO request) {
        try {
            // 활동을 처리하고 포인트를 업데이트
            memberBadgeManager.processActivity(request.getMemberId(), request.getActivityType());
            // 뱃지 등급은 자동으로 업데이트되므로, 현재 점수만 조회
            BigDecimal currentPoints = badgeService.getCurrentPoints(request.getMemberId());
            
            // 성공 응답
            return ResponseEntity.ok(Map.of(
                "message", String.format("점수 반영 완료: %s, 회원 ID: %d", request.getActivityType(), request.getMemberId()),
                "currentPoints", currentPoints
            ));
        } catch (IllegalArgumentException e) {
            // 잘못된 요청에 대한 응답 반환
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // 예기치 않은 오류에 대한 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
        }
    }

    // 뱃지 생성 엔드포인트
    @PostMapping("/create")
    public ResponseEntity<String> createBadge(@RequestBody MemberBadgeRequestDTO badgeRequest) {
        try {
            badgeService.createBadge(badgeRequest.getMember_id());
            return ResponseEntity.status(HttpStatus.CREATED).body("뱃지 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("뱃지 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 1. 사용자 점수 및 등급 조회
    @GetMapping("/user/{memberId}/status")
    public ResponseEntity<Map<String, Object>> getUserBadgeStatus(@PathVariable("memberId") int memberId) {
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
    public ResponseEntity<String> updateUserPoints(@PathVariable("memberId") int memberId, @RequestBody BigDecimal points) {
        badgeService.updateUserPoints(memberId, points);
        return ResponseEntity.ok("점수가 업데이트되었습니다.");
    }

    // 3. 사용자 점수 순위 통계
    @GetMapping("/user/rankings")
    public ResponseEntity<List<Map<String, Object>>> getUserRankings() {
        List<Map<String, Object>> rankings = badgeService.getUserRankings();
        return ResponseEntity.ok(rankings);
    }

    // 4. 사용자 활동 기록 조회
  
@GetMapping("/user/{memberId}/activities")
public ResponseEntity<?> getUserActivities(@PathVariable("memberId") int memberId) {
    try {
        List<UserActivityRecordResponseDTO> activities = userActivityRecordMapper.getActivitiesByMemberId(memberId);

        // activities가 null이거나 비어있을 경우
        if (activities == null || activities.isEmpty()) { // 논리 연산자 '||' 사용
            // 데이터가 없을 경우 메시지와 함께 200 OK 반환
            return ResponseEntity.ok(Map.of(
                "message", "활동 기록이 없습니다.",
                "activities", Collections.emptyList()
            ));
        }

        return ResponseEntity.ok(activities);

    } catch (Exception e) {
        logger.error("사용자 활동 조회 중 오류 발생: ", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "활동 기록 조회 중 오류가 발생했습니다."));
    }
}


    // 5. 사용자 뱃지 정보 조회
    @GetMapping("/user/{memberId}/badge")
    public ResponseEntity<MemberBadgeResponseDTO> getUserBadge(@PathVariable("memberId") int memberId) {
        MemberBadgeResponseDTO badge = memberBadgeMapper.getBadgeByMemberId(memberId);
        return ResponseEntity.ok(badge);
    }

    // 전역 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "예기치 않은 오류가 발생했습니다."));
    }

    // 회원의 모든 활동 처리
    @PostMapping("/user/{memberId}/activities")
    public ResponseEntity<Map<String, Object>> processUserActivities(@PathVariable("memberId") int memberId) {
        try {
            badgeService.processUserActivities(memberId);
            // 모든 활동 처리 후 현재 점수와 뱃지 등급 조회
            BigDecimal currentPoints = badgeService.getCurrentPoints(memberId);
            String badgeLevel = badgeService.getBadgeLevel(currentPoints);
            
            return ResponseEntity.ok(Map.of(
                "message", "회원의 모든 활동이 처리되었습니다.",
                "currentPoints", currentPoints,
                "badgeLevel", badgeLevel
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "활동 처리 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // 활동 추가 엔드포인트
    @PostMapping("/user/{memberId}/addActivity")
    public ResponseEntity<Map<String, Object>> addActivity(@PathVariable("memberId") int memberId, @RequestBody UserActivityRecordRequestDTO request) {
        try {
            // 활동을 처리하고 포인트를 업데이트
            request.setMemberId(memberId); // 요청에서 회원 ID 설정
            userActivityRecordMapper.insertActivityAndUpdatePoints(Map.of(
                "activityType", request.getActivityType(),
                "memberId", memberId
            ));

            // 현재 점수 조회
            BigDecimal currentPoints = badgeService.getCurrentPoints(memberId);
            
            // 성공 응답
            return ResponseEntity.ok(Map.of(
                "message", String.format("활동 추가 완료: %s, 회원 ID: %d", request.getActivityType(), memberId),
                "currentPoints", currentPoints
            ));
        } catch (IllegalArgumentException e) {
            // 잘못된 요청에 대한 응답 반환
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // 예기치 않은 오류에 대한 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "addActivity : 예기치 않은 오류가 발생했습니다."));
        }
    }

    @GetMapping("/getBadge")
public ResponseEntity<Map<String, Object>> getBadgeIdPoint(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7); // "Bearer " 접두사 제거
        int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

        try {
            return memberBadgeManager.getPoint(userId);
        } catch (Exception e) {
            logger.error("사용자 뱃지 정보 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "사용자 뱃지 정보 조회 중 오류가 발생했습니다."));
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", "Invalid or missing authorization header"));
    }
}


    @GetMapping("/getrankpercentage")
    public ResponseEntity<Map<String, Object>> getRankPercentage(@RequestParam("member_id") int memberId) {
        Map<String, Object> response = memberBadgeMapper.getrankpercentage(memberId);
        return ResponseEntity.ok(response);
    }

}
// develop commit 용 주석
