package com.multipjt.multi_pjt.crew.ctrl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;
import com.multipjt.multi_pjt.crew.service.CrewService;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/crew")
public class CrewController {
    
    @Autowired
    private CrewService crewService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // --------- 크루 찾기 ---------

    // 크루 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createCrew(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewRequestDTO param) {

        System.out.println("client endpoint: /crew/create");
        System.out.println("debug>>> createCrew + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            crewService.createCrew(param);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 크루 리스트 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<CrewResponseDTO>> getCrewList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        System.out.println("client endpoint: /crew/list");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(crewService.getCrewList());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }


    // 크루 리스트 종목 조회
    @GetMapping("/list/sport")
    public ResponseEntity<List<CrewResponseDTO>> getCrewSportList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam Map<String, String> map) {

        System.out.println("client endpoint: /crew/list/sport");
        System.out.println("debug>>> getCrewSportList + " + map);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(crewService.getCrewSportList(map));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 특정 크루 정보 조회
    @GetMapping("/info/{crewId}")
    public ResponseEntity<CrewResponseDTO> getCrewInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("crewId") Integer crewId) {

        System.out.println("client endpoint: /crew/info/" + crewId);
        System.out.println("debug>>> getCrewInfo + " + crewId);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(crewService.getCrewInfo(crewId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 크루원 추가
    @PostMapping("/member/add")
    public ResponseEntity<Void> addCrewMember(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/member/add");
        System.out.println("debug>>> addCrewMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            crewService.addCrewMember(param);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 내가 속한 크루 조회
    @GetMapping("/my")
    public ResponseEntity<List<CrewResponseDTO>> getMyCrewList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("member_id") Integer member_id) {

        System.out.println("client endpoint: /crew/my");
        System.out.println("debug>>> getMyCrewList + " + member_id);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(crewService.getMyCrewList(member_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }


    // --------- 크루 소개 ---------

    // 크루 소개 수정
    @PatchMapping("/intro/update")
    public ResponseEntity<Void> updateCrewIntro(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewRequestDTO param) {

        System.out.println("client endpoint: /crew/intro/update");
        System.out.println("debug>>> updateCrewIntro + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            crewService.updateCrewIntro(param);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 크루 관리 수정
    @PatchMapping("/info/update")
    public ResponseEntity<Void> updateCrewInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewRequestDTO param) {

        System.out.println("client endpoint: /crew/info/update");
        System.out.println("debug>>> updateCrewInfo + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            crewService.updateCrewInfo(param);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }


    // 크루 삭제
    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<Void> deleteCrew(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("crewId") Integer crewId) {

        System.out.println("client endpoint: /crew/delete/" + crewId);
        System.out.println("debug>>> deleteCrew + " + crewId);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            crewService.deleteCrew(crewId, token_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }


    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    @PatchMapping("/member/approve")
    public ResponseEntity<Void> approveCrewMember(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/member/approve");
        System.out.println("debug>>> approveCrewMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            crewService.approveCrewMember(param, token_id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 크루원 조회
    @GetMapping("/member/list/{crewId}")
    public ResponseEntity<List<CrewMemberResponseDTO>> getCrewMemberList(
        @PathVariable("crewId") Integer crewId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        System.out.println("client endpoint: /crew/member/list/" + crewId);
        System.out.println("debug>>> getCrewMemberList + " + crewId);
        System.out.println("debug>>> Authorization Header: " + authHeader);
       
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(crewService.getCrewMemberList(crewId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 크루원 삭제
    @DeleteMapping("/member/delete")
    public ResponseEntity<Void> deleteCrewMember(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/member/delete");
        System.out.println("debug>>> deleteCrewMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            crewService.deleteCrewMember(param, token_id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }


    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    @PostMapping("/post/create")
    public ResponseEntity<Void> createCrewPost(@RequestBody CrewPostRequestDTO param) {
        System.out.println("client endpoint: /crew/post/create");
        System.out.println("debug>>> createCrewPost + " + param);
        crewService.createCrewPost(param);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // 크루 게시물 전체 조회
    @GetMapping("/post/list/{crewId}")
    public ResponseEntity<List<CrewPostResponseDTO>> getCrewPostList(@PathVariable("crewId") Integer crewId) {
        System.out.println("client endpoint: /crew/post/list/" + crewId);
        System.out.println("debug>>> getCrewPostList + " + crewId);
        return ResponseEntity.ok(crewService.getCrewPostList(crewId));
    }

    // 크루 게시물 상단 공지, 일반 고정 3개씩
    @GetMapping("/post/top")
    public ResponseEntity<List<CrewPostResponseDTO>> getCrewTopPostList(@RequestBody CrewPostRequestDTO params) {
        System.out.println("client endpoint: /crew/post/top/");
        System.out.println("debug>>> getCrewTopPostList + " + params);
        return ResponseEntity.ok(crewService.getCrewTopPostList(params));
    }

    // 크루 게시물 공지/인기/일반 조회
    @GetMapping("/post/notice")
    public ResponseEntity<List<CrewPostResponseDTO>> getCrewNoticePostList(@RequestBody CrewPostRequestDTO params) {
        System.out.println("client endpoint: /crew/post/notice/");
        System.out.println("debug>>> getCrewNoticePostList + " + params);
        return ResponseEntity.ok(crewService.getCrewNoticePostList(params));
    }

    // 크루 게시물 상세 조회
    @GetMapping("/post/detail")
    public ResponseEntity<CrewPostResponseDTO> getCrewPost(@RequestBody CrewPostRequestDTO params) {
        System.out.println("client endpoint: /crew/post/detail/");
        System.out.println("debug>>> getCrewPost + " + params);
        return ResponseEntity.ok(crewService.getCrewPost(params));
    }

    // 크루 게시물 수정
    @PatchMapping("/post/update")
    public ResponseEntity<Void> updateCrewPost(@RequestBody CrewPostRequestDTO param) {
        System.out.println("client endpoint: /crew/post/update");
        System.out.println("debug>>> updateCrewPost + " + param);
        crewService.updateCrewPost(param);
        return ResponseEntity.noContent().build();
    }

    // 크루 게시물 삭제
    @DeleteMapping("/post/delete")
    public ResponseEntity<Void> deleteCrewPost(@RequestBody CrewPostRequestDTO param) {
        System.out.println("client endpoint: /crew/post/delete");
        System.out.println("debug>>> deleteCrewPost + " + param);
        crewService.deleteCrewPost(param);
        return ResponseEntity.noContent().build();
    }

    // --------- 크루 댓글 ---------

    // 크루 댓글 작성
    @PostMapping("/comment/create")
    public ResponseEntity<Void> createCrewComment(@RequestBody CrewCommentsRequestDTO param) {
        System.out.println("client endpoint: /crew/comment/create");
        System.out.println("debug>>> createCrewComment + " + param);
        crewService.createCrewComment(param);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    // 크루 댓글 조회
    @GetMapping("/comment/list")
    public ResponseEntity<List<CrewCommentsResponseDTO>> getCrewCommentList(@RequestBody CrewCommentsRequestDTO param) {
        System.out.println("client endpoint: /crew/comment/list");
        System.out.println("debug>>> getCrewCommentList + " + param);
        return ResponseEntity.ok(crewService.getCrewCommentList(param));
    }

    // 크루 댓글 삭제
    @DeleteMapping("/comment/delete")
    public ResponseEntity<Void> deleteCrewComment(@RequestBody CrewCommentsRequestDTO param) {
        System.out.println("client endpoint: /crew/comment/delete");
        System.out.println("debug>>> deleteCrewComment + " + param);
        crewService.deleteCrewComment(param);
        return ResponseEntity.noContent().build();
    }
}
