package com.multipjt.multi_pjt.community.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;

@Mapper
public interface CommentMapper {

    // 댓글 등록
    public int commentInsert(CommentRequestDTO cdto);
    
     // 게시글 번호로 조회
    public List<CommentResponseDTO> getPostById(@Param("post_id") int pid);

    // 댓글 삭제
    public int commentDelete(Map<String, Integer> map);

    // 댓글 삭제 확인
    public CommentResponseDTO getCommentById(int cid);


    
} 
