package com.multipjt.multi_pjt.posts.comment;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.community.dao.CommentMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;


@SpringBootTest
public class CommentMapperApplicationTests {
    
    @Autowired
    private CommentMapper commentMapper;

    @Test
    @DisplayName("Com001: 댓글 작성")
    public void commentInsertTest(){
        
        // Given : 댓글 등록 기본 세팅
        CommentRequestDTO request = new CommentRequestDTO();
        
        request.setComments_contents("첫번째 댓글 입력입니다.");
        request.setComments_date(new Date());
        request.setMember_id(1);
        request.setPost_id(6);

        // When : 댓글 등록 동작 수행
        int rowsAffected = commentMapper.commentInsert(request);
        Assertions.assertEquals(1, rowsAffected, "댓글이 등록 되었습니다.");

        // Then : 데이터베이스에서 등록된 사용자 정보 조회 및 검증
        List<CommentResponseDTO> registeredComments = commentMapper.getPostById(6);     
        
        
        // Null 여부 확인
        Assertions.assertNotNull(registeredComments, "댓글 등록 성공");
        
        CommentResponseDTO registeredComment = registeredComments.get(0);

        // 일치 여부 확인
        Assertions.assertEquals("첫번째 댓글 입력입니다.", registeredComment.getComments_contents(), "댓글 내용이 일치해야합니다.");
        Assertions.assertEquals(1, registeredComment.getMember_id(), "회원 번호가 일치해야 합니다.");
        Assertions.assertEquals(6, registeredComment.getPost_id(), "게시글 번호가 일치해야 합니다.");

    }   


    @Test
    @DisplayName("Com002: 댓글 삭제")
    public void commentDeleteTest(){
        
        // Given : 댓글 삭제 기본 세팅
        CommentRequestDTO request = new CommentRequestDTO();
        

        // member_id and comment_id 로 삭제
        request.setMember_id(1);
        request.setComments_id(5);
        

        // When : 댓글 삭제 수행
        int rowsAffected = commentMapper.commentDelete(request);
        Assertions.assertEquals(1, rowsAffected, "댓글이 삭제 되어야 합니다.");

        // Then : 삭제된 댓글 정보 조회 - null 확인
        CommentResponseDTO deleteComment = commentMapper.getCommentById(5);  
        
        // null이 아닐 경우 테스트 실패
        Assertions.assertNull(deleteComment, "삭제한 댓글의 정보는 조회할 수 없어야 합니다.");
    }   

}
