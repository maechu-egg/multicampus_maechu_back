package com.multipjt.multi_pjt.crew.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
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
        System.out.println("debug>>> createCrewBattle + " + param);
        crewBattleService.createCrewBattle(param);
        return ResponseEntity.ok().build();
    }

    // --------- 배틀 상세보기 ---------

    // 피드 작성
    @PostMapping("/feed/create")
    public ResponseEntity<Void> createCrewBattleFeed(@RequestBody CrewBattleFeedRequestDTO param) {
        System.out.println("client endpoint: /crew/battle/feed/create");
        System.out.println("debug>>> createCrewBattleFeed + " + param);
        crewBattleService.createCrewBattleFeed(param);
        return ResponseEntity.ok().build();
    }
}
