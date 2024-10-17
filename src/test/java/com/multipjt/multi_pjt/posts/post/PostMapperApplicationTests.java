package com.multipjt.multi_pjt.posts.post;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;


@SpringBootTest
public class PostMapperApplicationTests {
    
    @Autowired
    private PostMapper postMapper;

    @Test
    @DisplayName("P01: 게시글 작성 ")
    public void postInsertTest(){
        
        // Given : 게시글 등록 기본 세팅
        PostRequestDTO request = new PostRequestDTO();
        UserRequestDTO user = new UserRequestDTO();

        user.setMember_id("1");

        request.setPost_contents("첫번째 게시글입니다.");
        request.setPost_title("첫번째 테스트");
        request.setPost_date(new Date());
        request.setPost_hashtag("#오늘은 #화이팅");
        request.setPost_img1("/first/postimg1");
        request.setPost_sport("복싱");
        request.setPost_sports_keyword("근력 운동");
        request.setMember_id(1);

        // When : 게시글 등록 동작 수행
        int rowsAffected = postMapper.postInsert(request);
        Assertions.assertEquals(1, rowsAffected, "게시글이 등록 되었습니다.");

        // Then : 데이터베이스에서 등록된 사용자 정보 조회 및 검증
        List<PostResponseDTO> registeredPosts = postMapper.getMemberById(1);     
        
        
        // Null 여부 확인
        Assertions.assertNotNull(registeredPosts, "게시글 등록 성공1");
        
        PostResponseDTO registeredPost = registeredPosts.get(0);

        // 일치 여부 확인
        Assertions.assertEquals("첫번째 게시글입니다.", registeredPost.getPost_contents(), "게시글 내용이 일치해야합니다.");
        Assertions.assertEquals("#오늘은 #화이팅", registeredPost.getPost_hashtag(), "헤시테그 값이 일치해야 합니다.");
        Assertions.assertEquals("/first/postimg1", registeredPost.getPost_img1(), "이미지1이 일치해야 합니다.");
        Assertions.assertEquals("복싱", registeredPost.getPost_sport(), "운동 종목이 일치해야 합니다.");
        Assertions.assertEquals("근력 운동", registeredPost.getPost_sports_keyword(), "키워드가 일치해야 합니다.");



    }





}
