package com.multipjt.multi_pjt.crew.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.service.CrewBattleService;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/crew/battle")
public class CrewBattleController {
    @Autowired
    private CrewBattleService crewBattleService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // --------- 크루 배틀 ---------

    // 배틀 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createCrewBattle(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewBattleRequestDTO param) {

        System.out.println("client endpoint: /crew/battle/create");
        System.out.println("debug: createCrewBattle + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewBattleService.createCrewBattle(param, token_id);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (ResponseStatusException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", e.getStatusCode().value());
                errorResponse.put("message", e.getReason());
                return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // 배틀 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getCrewBattleList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam("crew_id") Integer crew_id) {

        System.out.println("client endpoint: /crew/battle/list");
        System.out.println("debug: getCrewBattleList + " + crew_id);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewBattleResponseDTO> crewBattleList = crewBattleService.selectCrewBattle(crew_id, token_id);
                return ResponseEntity.ok(crewBattleList);
            } catch (ResponseStatusException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", e.getStatusCode().value());
                errorResponse.put("message", e.getReason());
                return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 특정 배틀 상세 조회
    @GetMapping("/list/detail")
    public ResponseEntity<?> getCrewBattleDetail(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody CrewBattleRequestDTO param) {

        System.out.println("client endpoint: /crew/battle/detail");
        System.out.println("debug: getCrewBattleDetail + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                CrewBattleResponseDTO crewBattleDetail = crewBattleService.selectCrewBattleDetail(param, token_id);
                return ResponseEntity.ok(crewBattleDetail);
            } catch (ResponseStatusException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", e.getStatusCode().value());
                errorResponse.put("message", e.getReason());
                return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 배틀 참가
    @PostMapping("/member/join")
    public ResponseEntity<Map<String, Object>> createBattleMember(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody BattleMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/battle/member/join");
        System.out.println("debug: createBattleMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewBattleService.createBattleMember(param, token_id);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (ResponseStatusException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", e.getStatusCode().value());
                errorResponse.put("message", e.getReason());
                return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // --------- 배틀 피드보기 ---------

    // 배틀 참가 멤버 조회
    @GetMapping("/member/list")
    public ResponseEntity<List<BattleMemberResponseDTO>> getBattleMemberList(@RequestParam("battle_id") Integer battle_id) {
        System.out.println("client endpoint: /crew/battle/member/list");
        System.out.println("debug: getBattleMemberList + " + battle_id);
        return ResponseEntity.ok(crewBattleService.selectBattleMember(battle_id));
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
