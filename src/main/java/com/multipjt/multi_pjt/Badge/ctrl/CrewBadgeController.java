package com.multipjt.multi_pjt.badge.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.service.CrewBadgeManager;

@RestController
@RequestMapping("/crew_badges")
public class CrewBadgeController {

    private final CrewBadgeManager crewBadgeManager;

    @Autowired
    public CrewBadgeController(CrewBadgeManager crewBadgeManager) {
        this.crewBadgeManager = crewBadgeManager;
    }

    // 크루 뱃지 생성
    @PostMapping("/crewbadge")
    public ResponseEntity<String> createCrewBadge(@RequestParam int member_id) {
        crewBadgeManager.processBattleWin(member_id); // 배틀 승리 처리
        return ResponseEntity.ok("크루 뱃지가 생성되었습니다.");
    }

    // 특정 크루원의 뱃지 정보 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<CrewBadgeResponseDTO> getCrewBadge(@PathVariable int memberId) {
        CrewBadgeResponseDTO badge = crewBadgeManager.selectCrewBadgeByMemberId(memberId);
        if (badge == null) {
            return ResponseEntity.notFound().build(); // 뱃지가 없을 경우 404 반환
        }
        return ResponseEntity.ok(badge);
    }

    // 크루 뱃지 업데이트
    @PutMapping
    public ResponseEntity<String> updateCrewBadge(@RequestBody CrewBadgeRequestDTO badgeRequest) {
        crewBadgeManager.updateBadge(badgeRequest);
        return ResponseEntity.ok("크루 뱃지가 업데이트되었습니다.");
    }
}
