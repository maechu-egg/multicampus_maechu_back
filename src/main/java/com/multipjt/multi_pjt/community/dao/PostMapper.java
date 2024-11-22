package com.multipjt.multi_pjt.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;



@Mapper
public interface PostMapper {
    
    // 게시글 등록
    public int postInsert(PostRequestDTO pdto);

    //  member_id로 게시글 조회
    List<PostRequestDTO> getMemberById(@Param("member_id") int mid);

    // 게시글 수정
    public int postUpdate(PostRequestDTO pdto);

    // 게시글 이미지 선택
    public List<String> postImgsSelect(Map<String, Integer> map);

    // 게시글 수정 post 조회
    public PostResponseDTO updatePostResult(PostRequestDTO pdto);

    // 게시글 삭제
    public int postDelete(Map<String, Integer> map);

    // 게시글 번호로 조회
    PostResponseDTO getPostById(@Param("post_id") int pid);
    

    // 단어 검색 - 글 제목 / 글 내용 / 해시태그  
    List<PostResponseDTO> postSelectTCH(Map<String, Object> map);

    int countSearchPosts(Map<String, Object> map);

    // 현재 게시글의 좋아요 수 조회
    int postLikeCount(@Param("post_id") int pid);
    
    // 전체 페이지 
    List<PostResponseDTO> postAllSelect(Map<String, Object> map);

    // 전체 페이지 수
    int countPosts(Map<String, Object> map);


    // 상세 페이지
    List<PostResponseDTO> postDetailSelectTest(Map<String, Integer> map);
    public PostResponseDTO          postDetailSelect(Map<String, Object> map);

    public void recommendationClick(Map<String, Object> map);


    // 댓글
    public List<CommentResponseDTO> postDetailComment(Map<String, Object> map);
    

    // Memberid 구하기
    int getMemberid(String userid);

    // 조회수 
    int postViewCount(Map<String, Integer> map) ;
    

    // new 회원 / 활동 없는 회원 판단
    int isNewMember(int member_id);

    // new 회원 가입 / 활동이 오래된 사람 에게 추천 list
    List<PostResponseDTO> newMemberRCPost(Map<String, Object> map);

    // 대분류 조회
    String searchUpPost(String post_sport);
   
    // 활동 있는 회원 post_sport, post_up_sport 뽑기
    List<PostResponseDTO> exMemberData(int member_id);

    // 대분류 소분류로 인기 게시글 추천
    List<PostResponseDTO> findRCPosts(Map<String, Object> map);
    
    //  회원의 관심 운동 
    List<String> interestSport(Map<String, Object> map);

    // 제외 
    List<Integer> notinPostID(int member_id);

    // 추천 테이블에 넣기
    int insertRec(Map<String, Object> map);

    // 비회원 추천 
    List<PostResponseDTO> nonMemberRCPost();

    // 키워드 검색
    List<PostResponseDTO> searchKeyword(Map<String, Object> map);

     // 전체 페이지 수
     int countKeyword(Map<String, Object> map);

    
    // 오운완 검색
    List<PostResponseDTO> searchToday();

    // 중고장터 
    List<PostResponseDTO> searchMarketplace();

    // 사용자 본인 게시글 검색 
    List<PostResponseDTO> myPosts(int member_id);


     // 사용자 본인 좋아요 게시글 검색 
     List<PostResponseDTO> myLikePosts(int member_id);
}