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

    // 게시글 번호로 조회
    PostResponseDTO getPostById(@Param("post_id") int pid);

    // 운동 종목 별 게시글 조회
    List<PostResponseDTO> postSelectSport(Map<String, Object> map);
    

    // 단어 검색 - 글 제목 / 글 내용 / 해시태그  
    List<PostResponseDTO> postSelectTCH(Map<String, Object> map);

    // 현재 게시글의 좋아요 수 조회
    int postLikeCount(@Param("post_id") int pid);
    
    // 전체 페이지 
    List<PostResponseDTO> postAllSelect(@Param("size") int size, @Param("offset") int offset);

    // 전체 페이지 수
    int countPosts();


    // 상세 페이지
    List<PostResponseDTO> postDetailSelectTest(Map<String, Integer> map);
    public PostResponseDTO          postDetailSelect(Map<String, Integer> map);

    public List<CommentResponseDTO> postDetailComment(Map<String, Integer> map);
    

    // Memberid 구하기
    int getMemberid(String userid);

    // 조회수 
    int postViewCount(Map<String, Integer> map) ;
    

    // new 회원 / 활동 없는 회원 판단
    int isNewMember(int member_id);

    // new 회원 가입 / 활동이 오래된 사람 에게 추천 list
    List<PostResponseDTO> newMemberRCPost(int member_id);
   
    // 활동 있는 회원 post_sport, post_sport_keyword 뽑기
    List<PostResponseDTO> exMemberData(int member_id);

    // 운동 종목, 키워드로 RC
    List<PostResponseDTO> findRCPosts(String postSport, String postSportsKeyword, int slimit);
    

    // 비회원 추천 
    List<PostResponseDTO> nonMemberRCPost();

}