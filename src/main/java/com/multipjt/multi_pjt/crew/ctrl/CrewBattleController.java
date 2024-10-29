package com.multipjt.multi_pjt.crew.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.service.CrewBattleService;

@RestController
@RequestMapping("/crew/battle")
public class CrewBattleController {
    @Autowired
    private CrewBattleService crewBattleService;

    // --------- 크루 배틀 ---------

    // 배틀 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createCrewBattle(@RequestBody CrewBattleRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/create");
        System.out.println("debug: createCrewBattle + " + param);
        crewBattleService.createCrewBattle(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 배틀 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<CrewBattleResponseDTO>> getCrewBattleList(@RequestParam("crew_id") Integer crew_id) {
        System.out.println("client endpoint: /crew/battle/list");
        System.out.println("debug: getCrewBattleList + " + crew_id);
        return ResponseEntity.ok(crewBattleService.selectCrewBattle(crew_id));
    }

    // 배틀 참가
    @PostMapping("/member/join")
    public ResponseEntity<Void> createBattleMember(@RequestBody BattleMemberRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/member/join");
        System.out.println("debug: createBattleMember + " + param);
        crewBattleService.createBattleMember(param);
        return ResponseEntity.ok().build();
    }

    // --------- 배틀 상세보기 ---------

    // 배틀 참가 멤버 조회
    @GetMapping("/member/list")
    public ResponseEntity<List<BattleMemberResponseDTO>> getBattleMemberList(@RequestParam("battle_id") Integer battle_id) {
        System.out.println("client endpoint: /crew/battle/member/list");
        System.out.println("debug: getBattleMemberList + " + battle_id);
        List<BattleMemberResponseDTO> members = crewBattleService.selectBattleMember(battle_id);
        // 뱃지 이미지 경로 설정
        for (BattleMemberResponseDTO badge : members) {
            String badgeImagePath = badge.getBadgeImagePath();
            badge.setBadgeImagePath(badgeImagePath);
        }
        return ResponseEntity.ok(members);
    }

    // 피드 작성
    @PostMapping("/feed/create")
    public ResponseEntity<Void> createCrewBattleFeed(@RequestBody CrewBattleFeedRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/feed/create");
        System.out.println("debug: createCrewBattleFeed + " + param);
        crewBattleService.createCrewBattleFeed(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 피드 조회
    @GetMapping("/feed/list")
    public ResponseEntity<List<CrewBattleFeedResponseDTO>> getCrewBattleFeedList(@RequestParam("participant_id") Integer param) {
        System.out.println("client endpoint: /crew/battle/feed/list");
        System.out.println("debug: getCrewBattleFeedList + " + param);
        return ResponseEntity.ok(crewBattleService.selectCrewBattleFeed(param));
    }

    // 투표
    @PostMapping("/vote/create")
    public ResponseEntity<Void> createVote(@RequestBody CrewVoteRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/vote/create");
        System.out.println("debug: createVote + " + param);
        crewBattleService.createVote(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
