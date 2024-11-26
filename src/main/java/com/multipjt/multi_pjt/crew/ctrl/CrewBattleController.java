package com.multipjt.multi_pjt.crew.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
        @RequestParam("crew_id") Integer crew_id,
        @RequestParam("battle_id") Integer battle_id) {

        System.out.println("client endpoint: /crew/battle/detail");
        System.out.println("debug: getCrewBattleDetail + crew_id: " + crew_id + ", battle_id: " + battle_id);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                CrewBattleResponseDTO crewBattleDetail = crewBattleService.selectCrewBattleDetail(crew_id, battle_id, token_id);
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

    // 사용자 참여 배틀 조회
    @GetMapping("/list/my")
    public ResponseEntity<?> getCrewBattleByMemberId(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) { 

        System.out.println("client endpoint: /crew/battle/list/my");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewBattleResponseDTO> myBattle = crewBattleService.selectMyBattle(token_id);
                return ResponseEntity.ok(myBattle);
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
    public ResponseEntity<?> getBattleMemberList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam("crew_id") Integer crew_id,
        @RequestParam("battle_id") Integer battle_id) {

        System.out.println("client endpoint: /crew/battle/member/list");
        System.out.println("debug: getBattleMemberList + crew_id: " + crew_id + ", battle_id: " + battle_id);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<BattleMemberResponseDTO> battleMemberList = crewBattleService.selectBattleMember(crew_id, battle_id, token_id);
                return ResponseEntity.ok(battleMemberList);
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

    // 피드 작성
    @PostMapping(value = "/feed/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createCrewBattleFeed(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @ModelAttribute CrewBattleFeedRequestDTO param,
        @RequestParam(value = "ImgFile", required = false) MultipartFile ImgFile) {

        System.out.println("client endpoint: /crew/battle/feed/create");
        System.out.println("debug: createCrewBattleFeed + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewBattleService.createCrewBattleFeed(param, token_id, ImgFile);
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

    // 피드 조회
    @GetMapping("/feed/list")
    public ResponseEntity<?> getCrewBattleFeedList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestParam("participant_id") int param,
        @RequestParam("crew_id") int crew_id) {

        System.out.println("client endpoint: /crew/battle/feed/list");
        System.out.println("debug: getCrewBattleFeedList + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewBattleFeedResponseDTO> crewBattleFeedList = crewBattleService.selectCrewBattleFeed(crew_id, param, token_id);
                return ResponseEntity.ok(crewBattleFeedList);
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

    // 투표
    @PostMapping("/vote/create")
    public ResponseEntity<Map<String, Object>> createVote(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
        @RequestBody CrewVoteRequestDTO param,
        @RequestParam("crew_id") int crew_id) {

        System.out.println("client endpoint: /crew/battle/vote/create");
        System.out.println("debug: createVote + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewBattleService.createVote(param, token_id, crew_id);
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
}
