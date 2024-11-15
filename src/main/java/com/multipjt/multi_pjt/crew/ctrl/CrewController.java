package com.multipjt.multi_pjt.crew.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostLikeRequestDTO;
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

    // 추천 크루 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<CrewResponseDTO>> getCrewList(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("client endpoint: /crew/list");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            return ResponseEntity.ok(crewService.getCrewList(token_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // 추천 크루 리스트 조회 limit 3
    @GetMapping("/list/homepage")
    public ResponseEntity<?> getCrewListForHomepage(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("client endpoint: /crew/list/homepage");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                return ResponseEntity.ok(crewService.getCrewListForHomepage(token_id));
            } catch (ResponseStatusException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", e.getStatusCode().value());
                errorResponse.put("message", e.getReason());
                return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
            }
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

    // 크루원 신청
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
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        System.out.println("client endpoint: /crew/my");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            return ResponseEntity.ok(crewService.getMyCrewList(token_id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    @PatchMapping("/intro/update")
    public ResponseEntity<Map<String, Object>> updateCrewIntro(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @ModelAttribute CrewRequestDTO param,
            @RequestParam(value = "ImgFile", required = false) MultipartFile ImgFile) {

        System.out.println("client endpoint: /crew/intro/update");
        System.out.println("debug>>> updateCrewIntro + " + param);
        System.out.println("debug>>> crew_id 타입: " + ((Object)param.getCrew_id()).getClass().getName());
        System.out.println("debug>>> updateCrewIntro + " + ImgFile);

        if (ImgFile == null) {
            System.out.println("debug>>> ImgFile is null");
            return ResponseEntity.badRequest().body(Map.of("message", "이미지 파일이 필요합니다."));
        }
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int user_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.updateCrewIntro(param, user_id, ImgFile);
                return ResponseEntity.noContent().build();
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


    // 크루 관리 수정
    @PatchMapping("/info/update")
    public ResponseEntity<Map<String, Object>> updateCrewInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewRequestDTO param) {

        System.out.println("client endpoint: /crew/info/update");
        System.out.println("debug>>> updateCrewInfo + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.updateCrewInfo(param, token_id);
                return ResponseEntity.noContent().build();
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


    // 크루 삭제
    @DeleteMapping("/delete/{crewId}")
    public ResponseEntity<Map<String, Object>> deleteCrew(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("crewId") Integer crewId) {

        System.out.println("client endpoint: /crew/delete/" + crewId);
        System.out.println("debug>>> deleteCrew + " + crewId);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.deleteCrew(crewId, token_id);
                return ResponseEntity.noContent().build();
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


    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    @PatchMapping("/member/approve")
    public ResponseEntity<Map<String, Object>> approveCrewMember(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/member/approve");
        System.out.println("debug>>> approveCrewMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);
            
            try {
                crewService.approveCrewMember(param, token_id);
                return ResponseEntity.noContent().build();
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

    // 크루원 조회
    @GetMapping("/member/list/{crewId}")
    public ResponseEntity<?> getCrewMemberList(
        @PathVariable("crewId") Integer crewId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        System.out.println("client endpoint: /crew/member/list/" + crewId);
        System.out.println("debug>>> getCrewMemberList + " + crewId);
        System.out.println("debug>>> Authorization Header: " + authHeader);
       
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

            try {
                List<CrewMemberResponseDTO> crewMemberList = crewService.getCrewMemberList(crewId, token_id);
                return ResponseEntity.ok(crewMemberList);
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

    // 크루원 삭제
    @DeleteMapping("/member/delete")
    public ResponseEntity<Map<String, Object>> deleteCrewMember(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewMemberRequestDTO param) {

        System.out.println("client endpoint: /crew/member/delete");
        System.out.println("debug>>> deleteCrewMember + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

            try {
                crewService.deleteCrewMember(param, token_id);
                return ResponseEntity.noContent().build(); // 204 No Content
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


    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    @PostMapping(value = "/post/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createCrewPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @ModelAttribute CrewPostRequestDTO param,
            @RequestParam(value = "ImgFile", required = false) MultipartFile ImgFile) {

        System.out.println("client endpoint: /crew/post/create");
        System.out.println("debug>>> createCrewPost + " + param);
        System.out.println("debug>>> createCrewPost + " + ImgFile);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int token_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            
            try {
                crewService.createCrewPost(param, token_id, ImgFile);
                return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
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


    // 크루 게시물 전체 조회 (페이지네이션 적용)
    @GetMapping("/post/list/{crewId}")
    public ResponseEntity<?> getCrewPostList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("crewId") Integer crewId,
            @PageableDefault(size = 10) Pageable pageable) { // 기본 페이지 크기 설정

        System.out.println("client endpoint: /crew/post/list/" + crewId);
        System.out.println("debug>>> getCrewPostList + " + crewId);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                Page<CrewPostResponseDTO> crewPostPage = crewService.getCrewPostList(crewId, token_id, pageable);
                return ResponseEntity.ok(crewPostPage);
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

    // 크루 게시물 상단 공지, 인기 고정 3개씩 조회
    @GetMapping("/post/top")
    public ResponseEntity<?> getCrewTopPostList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewPostRequestDTO params) {

        System.out.println("client endpoint: /crew/post/top/");
        System.out.println("debug>>> getCrewTopPostList + " + params);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewPostResponseDTO> crewTopPostList = crewService.getCrewTopPostList(params, token_id);
                return ResponseEntity.ok(crewTopPostList);
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


    // 크루 게시물 공지/인기/일반 조회
    @GetMapping("/post/notice")
    public ResponseEntity<?> getCrewNoticePostList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewPostRequestDTO params) {

        System.out.println("client endpoint: /crew/post/notice/");
        System.out.println("debug>>> getCrewNoticePostList + " + params);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewPostResponseDTO> crewNoticePostList = crewService.getCrewNoticePostList(params, token_id);
                return ResponseEntity.ok(crewNoticePostList);
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

    // 크루 게시물 상세 조회
    @GetMapping("/post/detail")
    public ResponseEntity<?> getCrewPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewPostRequestDTO params) {

        System.out.println("client endpoint: /crew/post/detail/");
        System.out.println("debug>>> getCrewPost + " + params);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                CrewPostResponseDTO crewPost = crewService.getCrewPost(params, token_id);
                return ResponseEntity.ok(crewPost);
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

    // 크루 게시물 수정
    @PatchMapping("/post/update")
    public ResponseEntity<?> updateCrewPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @ModelAttribute CrewPostRequestDTO param,
            @RequestParam(value = "ImgFile", required = false) MultipartFile ImgFile) {

        System.out.println("client endpoint: /crew/post/update");
        System.out.println("debug>>> updateCrewPost + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.updateCrewPost(param, token_id, ImgFile);
                return ResponseEntity.noContent().build();
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

    // 크루 게시물 삭제
    @DeleteMapping("/post/delete")
    public ResponseEntity<?> deleteCrewPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewPostRequestDTO param) {

        System.out.println("client endpoint: /crew/post/delete");
        System.out.println("debug>>> deleteCrewPost + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.deleteCrewPost(param, token_id);
                return ResponseEntity.noContent().build();
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
    // --------- 크루 게시물 좋아요 ---------

    // 크루 게시물 좋아요 증가/감소
    @PatchMapping("/post/like/click")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewPostLikeRequestDTO param) {

        System.out.println("client endpoint: /crew/post/like");
        System.out.println("debug>>> toggleLike + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.toggleLike(param, token_id);
                return ResponseEntity.noContent().build();
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
    
    // 크루 게시물 좋아요 상태 확인
    @GetMapping("/post/like/check")
    public ResponseEntity<?> checkCrewPostLike(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("crew_post_id") int crew_post_id,
            @RequestParam("member_id") int member_id,
            @RequestParam("crew_id") int crew_id) {

        System.out.println("client endpoint: /crew/post/like/check");
        System.out.println("debug>>> checkCrewPostLike + " + crew_post_id);
        System.out.println("debug>>> checkCrewPostLike + " + member_id);
        System.out.println("debug>>> checkCrewPostLike + " + crew_id);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                boolean isLiked = crewService.checkCrewPostLike(crew_post_id, member_id, crew_id, token_id);
                return ResponseEntity.ok(isLiked);
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

    // --------- 크루 댓글 ---------

    // 크루 댓글 작성
    @PostMapping("/comment/create")
    public ResponseEntity<Map<String, Object>> createCrewComment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewCommentsRequestDTO param) {

        System.out.println("client endpoint: /crew/comment/create");
        System.out.println("debug>>> createCrewComment + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.createCrewComment(param, token_id);
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

    // 크루 댓글 조회
    @GetMapping("/comment/list")
    public ResponseEntity<?> getCrewCommentList(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("crew_post_id") int crewPostId,
            @RequestParam("crew_id") int crewId) {

        System.out.println("client endpoint: /crew/comment/list");
        System.out.println("debug>>> getCrewCommentList + " + crewPostId);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                List<CrewCommentsResponseDTO> crewCommentList = crewService.getCrewCommentList(crewPostId, crewId, token_id);
                return ResponseEntity.ok(crewCommentList);
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

    // 크루 댓글 삭제
    @DeleteMapping("/comment/delete")
    public ResponseEntity<?> deleteCrewComment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CrewCommentsRequestDTO param) {

        System.out.println("client endpoint: /crew/comment/delete");
        System.out.println("debug>>> deleteCrewComment + " + param);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            int token_id = jwtTokenProvider.getUserIdFromToken(token);

            try {
                crewService.deleteCrewComment(param, token_id);
                return ResponseEntity.noContent().build();
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
