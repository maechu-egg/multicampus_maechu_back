package com.multipjt.multi_pjt.crew.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
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
        return ResponseEntity.ok().build();
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    @PostMapping("/intro/update")
    public ResponseEntity<Void> updateCrewIntro(@RequestBody CrewRequestDTO param) {
        System.out.println("client endpoint: /crew/intro/update");
        System.out.println("debug>>> updateCrewIntro + " + param);
        crewService.updateCrewIntro(param);
        return ResponseEntity.ok().build();
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
