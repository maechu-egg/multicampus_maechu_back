package com.multipjt.multi_pjt.crew.service;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import com.multipjt.multi_pjt.config.FileService;
import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewCommentsResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostLikeRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewPostResponseDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

@Service
public class CrewService {
    
    @Autowired
    private CrewMapper crewMapper;
    
    @Autowired
    private FileService fileService; // FileService 주입
    
    // --------- 크루 찾기 ---------

    // 크루생성
    public void createCrew(CrewRequestDTO param) {
        System.out.println("debug>>> Service: createCrew + " + crewMapper);
        System.out.println("debug>>> Service: createCrew + " + param);
        
        // 크루 생성
        crewMapper.createCrewRow(param);
        
        // 크루장 추가
        CrewMemberRequestDTO crewMember = new CrewMemberRequestDTO();
        crewMember.setCrew_id(param.getCrew_id()); // 생성된 크루 ID
        crewMember.setMember_id(param.getMember_id()); // 크루장 ID
        crewMember.setCrew_member_state(1); // 크루장 상태

        crewMapper.insertCrewMemberRow(crewMember); // 크루 멤버 추가
    }

    // 추천 크루 리스트 조회
    public List<CrewResponseDTO> getCrewList(int token_id) {
        System.out.println("debug>>> Service: getCrewList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewList + " + token_id);

        // 정확한 지역과 일치하는 크루 조회
        List<CrewResponseDTO> exactMatchCrews = crewMapper.selectCrewRow(token_id);
        // 도 단위로 일치하는 크루 조회
        List<CrewResponseDTO> regionMatchCrews = crewMapper.selectCrewByRegionRow(token_id);
        // 정확한 지역과 일치하는 크루가 있으면 그 크루들만 조회
        if (!exactMatchCrews.isEmpty()) {
            return exactMatchCrews;
        // 정확한 지역과 일치하는 크루가 없으면 도 단위로 일치하는 크루 조회
        } else {
            return regionMatchCrews;
        }
    }

    // 추천 크루 리스트 조회 limit 3
    public List<CrewResponseDTO> getCrewListForHomepage(int token_id) {
        System.out.println("debug>>> Service: getCrewListForHomepage + " + crewMapper);
        System.out.println("debug>>> Service: getCrewListForHomepage + " + token_id);

        // 정확한 지역과 일치하는 크루 조회
        List<CrewResponseDTO> exactMatchCrews = crewMapper.selectCrewForHomepageRow(token_id);
        // 도 단위로 일치하는 크루 조회
        List<CrewResponseDTO> regionMatchCrews = crewMapper.selectCrewByRegionRowForHomepage(token_id);
        try {
            // 정확한 지역과 일치하는 크루가 3개 미만일 경우, 도 단위로 일치하는 크루를 추가로 가져옴
            if (exactMatchCrews.size() < 3) {
                // 이미 추가된 크루의 ID를 추적하기 위한 Set
                Set<Integer> existingCrewIds = new HashSet<>();
                exactMatchCrews.forEach(crew -> existingCrewIds.add(crew.getCrew_id()));

                for (CrewResponseDTO crew : regionMatchCrews) {
                    if (exactMatchCrews.size() >= 3) break;
                    // 중복되지 않는 경우에만 추가
                    if (!existingCrewIds.contains(crew.getCrew_id())) {
                        exactMatchCrews.add(crew);
                        existingCrewIds.add(crew.getCrew_id());
                    }
                }
            }
            return exactMatchCrews;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "가까운 지역에 크루 존재하지 않습니다.");
        }
    }

    // 특정 크루 정보 조회
    public CrewResponseDTO getCrewInfo(Integer crewId) {
        System.out.println("debug>>> Service: getCrewInfo + " + crewMapper);
        System.out.println("debug>>> Service: getCrewInfo + " + crewId);
        
        CrewResponseDTO crew = crewMapper.selectCrewInfoRow(crewId);
        return crew;
    }

    // 이미지 URL 생성 메서드
    private String getImageUrl(String Img) {
        return "/static/" + Img; // 정적 파일 경로에 맞게 URL 생성
    }

    // 크루원 신청
    public void addCrewMember(CrewMemberRequestDTO param) {
        System.out.println("debug>>> Service: addCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: addCrewMember + " + param);
        crewMapper.insertCrewMemberRow(param);
    }

    // 내가 속한 크루 조회
    public List<CrewResponseDTO> getMyCrewList(Integer token_id) {
        System.out.println("debug>>> Service: getMyCrewList + " + crewMapper);
        System.out.println("debug>>> Service: getMyCrewList + " + token_id);

        try {
            List<CrewResponseDTO> crewList = crewMapper.selectMyCrewRow(token_id);
            
            // 이미지 URL 설정
            crewList.forEach(crew -> {
                if (crew != null && crew.getCrew_intro_img() != null) {
                    crew.setCrew_intro_img(getImageUrl(crew.getCrew_intro_img()));
                }
            });

            return crewList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "내가 속한 크루가 없습니다.");
        }
    }

    // --------- 크루 소개 ---------

    // 크루 소개 수정
    public void updateCrewIntro(CrewRequestDTO param, int token_id, MultipartFile ImgFile) {
        System.out.println("debug>>> Service: updateCrewIntro + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewIntro + " + param);
        System.out.println("debug>>> Service: updateCrewIntro + " + token_id);
        System.out.println("debug>>> Service: updateCrewIntro + " + ImgFile);

        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());

        if (token_id == leaderId) {
            // 이미지 파일이 있는 경우에만 기존 이미지 파일 삭제 및 새 이미지 저장
            if (ImgFile != null && !ImgFile.isEmpty()) {
                // 기존 이미지 파일 삭제
                String currentImgFileName = crewMapper.selectCurrentCrewIntroImg(param.getCrew_id());
                if (currentImgFileName != null && !currentImgFileName.equals("CrewDefault")) {
                    fileService.deleteFileFromBucket(currentImgFileName);
                }
                // 버킷에 크루 소개 이미지 저장 및 파일 이름 반환
                String postFileName = fileService.putFileToBucket(ImgFile);
                // db에 새 이미지 파일 저장
                param.setCrew_intro_img(postFileName);
            } else {
                System.out.println("debug>>> Service: updateCrewIntro + 기존 이미지로 설정");
            }
            // 크루 소개 업데이트 로직 수행
            crewMapper.updateCrewIntroRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루 소개를 수정할 수 있습니다.");
        }
    }

    // 크루 관리 수정
    public void updateCrewInfo(CrewRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: updateCrewInfo + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewInfo + " + param);
        System.out.println("debug>>> Service: updateCrewInfo + " + token_id);
        
        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());

        if(token_id == leaderId) {
            crewMapper.updateCrewInfoRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루 관리를 수정할 수 있습니다.");
        }
    }

    // 크루 삭제
    public void deleteCrew(Integer crewId, Integer token_id) {
        System.out.println("debug>>> Service: deleteCrew + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrew + " + crewId);
        System.out.println("debug>>> Service: deleteCrew + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(crewId);

        if(token_id == leaderId) {
            crewMapper.deleteCrewRow(crewId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루를 삭제할 수 있습니다.");
        }
    }

    // --------- 크루원 정보 ---------

    // 크루원 가입 승인
    public void approveCrewMember(CrewMemberRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: approveCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: approveCrewMember + " + param);
        System.out.println("debug>>> Service: approveCrewMember + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());
        System.out.println("token_id: " + token_id + ", leaderId: " + leaderId);

        if(token_id == leaderId) {   
            crewMapper.updateCrewMemberRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장만 크루원 가입을 승인할 수 있습니다.");
        }
    }

    // 크루원 조회
    public List<CrewMemberResponseDTO> getCrewMemberList(Integer crewId, Integer token_id) {
        System.out.println("debug>>> Service: getCrewMemberList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewMemberList + " + crewId);
        System.out.println("debug>>> Service: getCrewMemberList + " + token_id);

        List<CrewMemberResponseDTO> crewMembers = crewMapper.selectCrewMemberRow(crewId);

        // 이미지 URL 설정
        crewMembers.forEach(member -> {
            if (member != null && member.getMember_img() != null) {
                member.setMember_img(getImageUrl(member.getMember_img()));
            }
        });

        return crewMembers;
    }

    // 크루원 삭제
    public void deleteCrewMember(CrewMemberRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: deleteCrewMember + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewMember + " + param);
        System.out.println("debug>>> Service: deleteCrewMember + " + token_id);

        int leaderId = crewMapper.selectCrewLeaderIdRow(param.getCrew_id());
        int memberId = param.getMember_id();

        if(token_id == leaderId || token_id == memberId) {
            crewMapper.deleteCrewMemberRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루장 또는 자신만 크루원을 삭제할 수 있습니다.");
        }
    }

    // --------- 크루 게시판 ---------

    // 크루 게시물 등록
    public void createCrewPost(CrewPostRequestDTO param, Integer token_id, MultipartFile ImgFile) {
        System.out.println("debug>>> Service: createCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: createCrewPost + " + param);
        System.out.println("debug>>> Service: createCrewPost + " + ImgFile);
        
        // 크루원인지, 승인 상태인지 확인
        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            if (ImgFile != null && !ImgFile.isEmpty()) {
                // 버킷에 크루 게시물 이미지 저장 및 파일 이름 반환
                String postFileName = fileService.putFileToBucket(ImgFile);
                // CrewRequestDTO에 파일 이름 설정
                param.setCrew_post_img(postFileName);
            }
            crewMapper.insertCrewPostRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 등록이 가능합니다.");
        }
    }

    // 크루 게시물 전체 조회 (페이지네이션 적용)
    // public Page<CrewPostResponseDTO> getCrewPostList(Integer crewId, Integer token_id, Pageable pageable) {
    public Page<CrewPostResponseDTO> getCrewPostList(Integer crewId, Integer token_id, Integer offset, Integer size) {
        System.out.println("debug>>> Service: getCrewPostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewPostList + " + crewId);
        System.out.println("debug>>> Service: getCrewPostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crewId).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_id", crewId); // crew_id를 Map에 추가
            // params.put("pageable", pageable); // pageable을 Map에 추가
            params.put("offset", offset);
            params.put("size", size);

            List<CrewPostResponseDTO> crewPostList = crewMapper.selectCrewPostListRow(params);
            // 이미지 URL 설정
            crewPostList.forEach(post -> {
                if (post != null && post.getCrew_post_img() != null) {
                    post.setCrew_post_img(getImageUrl(post.getCrew_post_img()));
                }
            });
            int pageNumber = offset / size;
            Pageable pageable = PageRequest.of(pageNumber, size);
            return new PageImpl<>(crewPostList, pageable, crewMapper.selectCrewPostListCountRow(crewId));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 상단 공지, 인기 고정 3개씩
    public List<CrewPostResponseDTO> getCrewTopPostList(int crew_id, int crew_post_state, Integer token_id) {
        System.out.println("debug>>> Service: getCrewTopPostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewTopPostList + " + crew_id);
        System.out.println("debug>>> Service: getCrewTopPostList + " + crew_post_state);
        System.out.println("debug>>> Service: getCrewTopPostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_id", crew_id);
            params.put("crew_post_state", crew_post_state);
            List<CrewPostResponseDTO> crewTopPostList = crewMapper.selectCrewTopPostRow(params);
            
            // 이미지 URL 설정
            crewTopPostList.forEach(post -> {
                if (post != null && post.getCrew_post_img() != null) {
                    post.setCrew_post_img(getImageUrl(post.getCrew_post_img()));
                }
            });
            return crewTopPostList;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 공지/인기/일반 조회
    public List<CrewPostResponseDTO> getCrewNoticePostList(int crew_id, int crew_post_state, Integer token_id) {
        System.out.println("debug>>> Service: getCrewNoticePostList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewNoticePostList + " + crew_id);
        System.out.println("debug>>> Service: getCrewNoticePostList + " + crew_post_state);
        System.out.println("debug>>> Service: getCrewNoticePostList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_id", crew_id);
            params.put("crew_post_state", crew_post_state);
            List<CrewPostResponseDTO> crewNoticePostList = crewMapper.selectCrewNoticePostRow(params);
            
            // 이미지 URL 설정
            crewNoticePostList.forEach(post -> {
                if (post != null && post.getCrew_post_img() != null) {
                    post.setCrew_post_img(getImageUrl(post.getCrew_post_img()));
                }
            });

            return crewNoticePostList;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 상세 조회
    public CrewPostResponseDTO getCrewPost(int crew_id, int crew_post_id, Integer token_id) {
        System.out.println("debug>>> Service: getCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: getCrewPost + " + crew_id);
        System.out.println("debug>>> Service: getCrewPost + " + crew_post_id);
        System.out.println("debug>>> Service: getCrewPost + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_id", crew_id);
            params.put("crew_post_id", crew_post_id);
            CrewPostResponseDTO crewPost = crewMapper.selectCrewPostRow(params);

            // 이미지 URL 설정
            if (crewPost != null && crewPost.getCrew_post_img() != null) {
                crewPost.setCrew_post_img(getImageUrl(crewPost.getCrew_post_img()));
            }
            return crewPost;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 조회가 가능합니다.");
        }
    }

    // 크루 게시물 수정
    public void updateCrewPost(CrewPostRequestDTO param, Integer token_id, MultipartFile ImgFile) {
        System.out.println("debug>>> Service: updateCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: updateCrewPost + " + param);
        System.out.println("debug>>> Service: updateCrewPost + " + token_id);
        System.out.println("debug>>> Service: updateCrewPost + " + ImgFile);

        int writerId = param.getMember_id();

        if (token_id == writerId) {
            // 이미지 파일이 있는 경우에만 기존 이미지 파일 삭제 및 새 이미지 저장
            if (ImgFile != null && !ImgFile.isEmpty()) {
                Map<String, Object> params = new HashMap<>();
                params.put("crew_id", param.getCrew_id());
                params.put("crew_post_id", param.getCrew_post_id());

                // 기존 이미지 파일 삭제
                String currentImgFileName = crewMapper.selectCrewPostRow(params).getCrew_post_img();
                if (currentImgFileName != null && !currentImgFileName.isEmpty()) {
                    fileService.deleteFileFromBucket(currentImgFileName);
                }
                // 버킷에 크루 게시물 이미지 저장 및 파일 이름 반환
                String postFileName = fileService.putFileToBucket(ImgFile);
                // DB에 파일 이름 설정
                param.setCrew_post_img(postFileName);
            } else {
               System.out.println("debug>>> Service: updateCrewPost + 기존 이미지로 설정");
            }
            crewMapper.updateCrewPostRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 게시물 수정이 가능합니다.");
        }
    }

    // 크루 게시물 삭제
    public void deleteCrewPost(int crew_id, int crew_post_id, int token_id) {
        System.out.println("debug>>> Service: deleteCrewPost + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewPost + " + crew_id);
        System.out.println("debug>>> Service: deleteCrewPost + " + crew_post_id);
        System.out.println("debug>>> Service: deleteCrewPost + " + token_id);

        Map<String, Object> params = new HashMap<>();
        params.put("crew_id", crew_id);
        params.put("crew_post_id", crew_post_id);
        params.put("member_id", token_id);
        
        int writerId = crewMapper.selectCrewPostRow(params).getMember_id();

        if(token_id == writerId) {
            crewMapper.deleteCrewPostRow(params);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 게시물 삭제가 가능합니다.");
        }
    }

    // --------- 크루 게시물 좋아요 ---------

    // 크루 게시물 좋아요 증가/감소
    public void toggleLike(CrewPostLikeRequestDTO param, int token_id) {

        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            // 좋아요 상태 확인
            Map<String, Object> params = new HashMap<>();
            params.put("crew_post_id", param.getCrew_post_id());
            params.put("member_id", param.getMember_id());
            boolean isLiked = crewMapper.selectCrewPostLikeRow(params).size() > 0;
            
            if (isLiked) {
                // 좋아요 취소
                crewMapper.decreasePostLikeRow(param.getCrew_post_id());
                crewMapper.deleteCrewPostLikeRow(param);
                System.out.println("좋아요 취소");
            } else {
                // 좋아요 추가
                crewMapper.increasePostLikeRow(param.getCrew_post_id());
                crewMapper.insertCrewPostLikeRow(param);
                System.out.println("좋아요 추가");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 좋아요가 가능합니다.");
        }
    }

    // 크루 좋아요 상태 확인
    public boolean checkCrewPostLike(int crew_post_id, int member_id, int crew_id, int token_id) {
        
        boolean isActiveMember = crewMapper.selectCrewMemberRow(crew_id).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_post_id", crew_post_id);
            params.put("member_id", member_id);
            return crewMapper.selectCrewPostLikeRow(params).size() > 0;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 게시물 좋아요 상태 확인이 가능합니다.");
        }
    }

    // --------- 크루 댓글 ---------

    // 크루 댓글 작성
    public void createCrewComment(CrewCommentsRequestDTO param, Integer token_id) {
        System.out.println("debug>>> Service: createCrewComment + " + crewMapper);
        System.out.println("debug>>> Service: createCrewComment + " + param);
        System.out.println("debug>>> Service: createCrewComment + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(param.getCrew_id()).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            crewMapper.insertCrewCommentRow(param);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 댓글 작성이 가능합니다.");
        }
    }

    // 크루 댓글 조회
    public List<CrewCommentsResponseDTO> getCrewCommentList(int crewPostId, int crewId, Integer token_id) {
        System.out.println("debug>>> Service: getCrewCommentList + " + crewMapper);
        System.out.println("debug>>> Service: getCrewCommentList + " + crewPostId);
        System.out.println("debug>>> Service: getCrewCommentList + " + crewId);
        System.out.println("debug>>> Service: getCrewCommentList + " + token_id);

        boolean isActiveMember = crewMapper.selectCrewMemberRow(crewId).stream()
            .anyMatch(member -> member.getMember_id() == token_id && member.getCrew_member_state() == 1);

        if (isActiveMember) {
            return crewMapper.selectCrewCommentsRow(crewPostId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "크루원만 댓글 조회가 가능합니다.");
        }
    }

    // 크루 댓글 삭제
    public void deleteCrewComment(int crew_comment_id, int crew_post_id, int token_id) {
        System.out.println("debug>>> Service: deleteCrewComment + " + crewMapper);
        System.out.println("debug>>> Service: deleteCrewComment + " + crew_comment_id);
        System.out.println("debug>>> Service: deleteCrewComment + " + crew_post_id);
        System.out.println("debug>>> Service: deleteCrewComment + " + token_id);

        // 특정 댓글 조회하여 작성자 확인
        CrewCommentsResponseDTO comment = crewMapper.selectCrewCommentById(crew_comment_id);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다.");
        }

        int writerId = comment.getMember_id();

        if (token_id == writerId) {
            Map<String, Object> params = new HashMap<>();
            params.put("crew_comment_id", crew_comment_id);
            params.put("crew_post_id", crew_post_id);
            params.put("member_id", token_id); // 현재 접속자

            crewMapper.deleteCrewCommentRow(params);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 댓글 삭제가 가능합니다.");
        }
    }
}
