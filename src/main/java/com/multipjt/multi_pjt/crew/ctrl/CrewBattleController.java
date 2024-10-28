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

import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
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

    // --------- 배틀 상세보기 ---------

    // 피드 작성
    @PostMapping("/feed/create")
    public ResponseEntity<Void> createCrewBattleFeed(@RequestBody CrewBattleFeedRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/feed/create");
        System.out.println("debug: createCrewBattleFeed + " + param);
        crewBattleService.createCrewBattleFeed(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
