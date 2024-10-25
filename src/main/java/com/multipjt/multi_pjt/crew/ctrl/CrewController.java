package com.multipjt.multi_pjt.crew.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;
import com.multipjt.multi_pjt.crew.service.CrewService;

@RestController
@RequestMapping("/crew")
public class CrewController {
    
    @Autowired
    private CrewService crewService;

    // --------- 크루 찾기 ---------

    // 크루 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createCrew(@RequestBody CrewRequestDTO param) {
        System.out.println("client endpoint: /crew/create");
        System.out.println("debug>>> createCrew + " + param);
        crewService.createCrew(param);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // 크루 리스트 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<CrewResponseDTO>> getCrewList() {
        System.out.println("client endpoint: /crew/list");
        return ResponseEntity.ok(crewService.getCrewList());
    }

    // 크루 리스트 종목 조회
    @GetMapping("/list/sport")
    public ResponseEntity<List<CrewResponseDTO>> getCrewSportList(@RequestParam Map<String, String> map) {
        System.out.println("client endpoint: /crew/list/sport");
        return ResponseEntity.ok(crewService.getCrewSportList(map));
    }

    // 특정 크루 정보 조회
    @GetMapping("/info/{crewId}")
    public ResponseEntity<CrewResponseDTO> getCrewInfo(@PathVariable("crewId") Integer crewId) {
        System.out.println("client endpoint: /crew/info/" + crewId);
        return ResponseEntity.ok(crewService.getCrewInfo(crewId));
    }

    // 크루원 추가
    @PostMapping("/member/add")
    public ResponseEntity<Void> addCrewMember(@RequestBody CrewMemberRequestDTO param) {
        System.out.println("client endpoint: /crew/member/add");
        System.out.println("debug>>> addCrewMember + " + param);
        crewService.addCrewMember(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    @PatchMapping("/intro/update")
    public ResponseEntity<Void> updateCrewIntro(@RequestBody CrewRequestDTO param) {
        System.out.println("client endpoint: /crew/intro/update");
        System.out.println("debug>>> updateCrewIntro + " + param);
        crewService.updateCrewIntro(param);
        return ResponseEntity.ok().build();
    }

    // 크루 관리 수정
    @PatchMapping("/info/update")
    public ResponseEntity<Void> updateCrewInfo(@RequestBody CrewRequestDTO param) {
        System.out.println("client endpoint: /crew/info/update");
        System.out.println("debug>>> updateCrewInfo + " + param);
        crewService.updateCrewInfo(param);
        return ResponseEntity.ok().build();
    }

    // 크루 삭제
    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<Void> deleteCrew(@PathVariable("crewId") Integer crewId) {
        System.out.println("client endpoint: /crew/delete/" + crewId);
        crewService.deleteCrew(crewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    @PatchMapping("/member/approve")
    public ResponseEntity<Void> approveCrewMember(@RequestBody CrewMemberRequestDTO param) {
        System.out.println("client endpoint: /crew/member/approve");
        crewService.approveCrewMember(param);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 크루원 조회
    @GetMapping("/member/list/{crewId}")
    public ResponseEntity<List<CrewMemberResponseDTO>> getCrewMemberList(@PathVariable("crewId") Integer crewId) {
        System.out.println("client endpoint: /crew/member/list/" + crewId);
        return ResponseEntity.ok(crewService.getCrewMemberList(crewId));
    }

    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    @PostMapping("/post/create")
    public ResponseEntity<Void> createCrewPost(@RequestBody CrewPostRequestDTO param) {
        System.out.println("client endpoint: /crew/post/create");
        System.out.println("debug>>> createCrewPost + " + param);
        crewService.createCrewPost(param);
        return ResponseEntity.ok().build();
    }


}
