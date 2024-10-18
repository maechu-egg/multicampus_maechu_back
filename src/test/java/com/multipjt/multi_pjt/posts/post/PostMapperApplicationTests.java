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

    @Test
    @DisplayName("P02: 게시글 수정 ")
    public void postUpdateTest(){
        
     
        //  Given : 게시글 조회 및 수정할 게시글 준비
        List<PostResponseDTO> selectPosts = postMapper.getMemberById(1);
        
        // 첫번째 게시글 선택
        PostResponseDTO selectPost = selectPosts.get(0);

        // When :  게시글 수정을 위한 세팅
        selectPost.setPost_contents("첫번째 게시글 수정 내용입니다.");
        selectPost.setPost_title("수정된 테스트 제목");
        selectPost.setPost_hashtag("#오늘은 #금요일");
        selectPost.setPost_img2("/update/post_img2");

        // 게시글 수정
        int postUpdate = postMapper.postUpdate(selectPost);

        // postUpdate가 1이면 이면 성공
        Assertions.assertEquals(1, postUpdate , "게시글이 수정되었습니다.");

        // Then: 수정된 게시글 정보 검증
        PostResponseDTO updatedPost = postMapper.getPostById(selectPost.getPost_id());
   
        // 게시글 확인
        Assertions.assertEquals("수정된 테스트 제목", updatedPost.getPost_title(), "게시글 제목이 수정되어야 합니다.");
        Assertions.assertEquals("첫번째 게시글 수정 내용입니다.", updatedPost.getPost_contents(), "게시글 내용이 수정 되어야 합니다.");
        Assertions.assertEquals("#오늘은 #금요일", updatedPost.getPost_hashtag(), "게시글 헤시테그 값이 수정 되어야 합니다.");
        Assertions.assertEquals("/update/post_img2", updatedPost.getPost_img2(), "이미지2이 수정 되어야 합니다.");
        
    }


    @Test
    @DisplayName("P03: 운동 종목 별 조회 ")
    public void postSelectSportTest(){
        
     
        //  Given : 게시글 조회 및 수정할 게시글 준비
        String sport = "헬스";
        
        
        // When :  게시글 수정을 위한 세팅
        List<PostResponseDTO> sportList = postMapper.postSelectSport(sport);
        
        // List 가 비어있는지 확인
        Assertions.assertFalse(sportList.isEmpty(), "게시글 조회에 실패했습니다.");


        // Then: 게시글 정보 검증
        PostResponseDTO first = sportList.get(0);
   
        // 게시글 확인
        Assertions.assertEquals("두 번째 테스트", first.getPost_title(), "게시글 제목이 조회 되어야 합니다.");
        Assertions.assertEquals("두 번째 운동은 상체 근력 운동입니다. 땀이 많이 났어요.", first.getPost_contents(), "게시글 내용이 조회 되어야 합니다.");
        Assertions.assertEquals("헬스", first.getPost_sport(), "운동 종목이 조회 되어야 합니다.");
        Assertions.assertEquals(1, first.getMember_id(), "회원 id가 조회 되어야 합니다.");
        
    }


}
