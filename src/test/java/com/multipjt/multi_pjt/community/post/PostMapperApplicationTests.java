package com.multipjt.multi_pjt.posts.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.dao.UserActivityMapper;
import com.multipjt.multi_pjt.community.domain.UserActivity.UserActivityRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;


@SpringBootTest
public class PostMapperApplicationTests {
    
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserActivityMapper userActivityMapper;

    @Test
    @DisplayName("P01: 게시글 작성 ")
    public void postInsertTest(){
        
        // Given : 게시글 등록 기본 세팅
        PostRequestDTO request = new PostRequestDTO();
        UserRequestDTO user = new UserRequestDTO();

        // user.setMember_id("1");

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
        
     
        //  Given : 게시글 조회 준비
        String sport = "헬스";
        
        
        // When :  게시글 조회를 위한 세팅
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

    @Test
    @DisplayName("P04: 제목/내욕/해시태그 검색 ")
    public void postSelectTCHTest(){
        
     
        //  Given : 게시글 검색어 준비
        String keyword = "오늘은";
        
        // When :  게시글 검색을 위한 세팅
        List<PostResponseDTO> searchList = postMapper.postSelectTCH(keyword);
        
        // List 가 비어있는지 확인
        Assertions.assertFalse(searchList.isEmpty(), "게시글 조회에 실패했습니다.");
        
        // Then: 게시글 정보 검증
        PostResponseDTO first = searchList.get(0);
   
        // 게시글 확인
        Assertions.assertEquals("수정된 테스트 제목", first.getPost_title(), "게시글 제목이 조회 되어야 합니다.");
        Assertions.assertEquals("첫번째 게시글 수정 내용입니다.", first.getPost_contents(), "게시글 내용이 조회 되어야 합니다.");
        Assertions.assertEquals("달리기", first.getPost_sport(), "운동 종목이 조회 되어야 합니다.");
        Assertions.assertEquals(1, first.getMember_id(), "회원 id가 조회 되어야 합니다.");
    
    }

    @Test
    @DisplayName("P05: 좋아요 수 조회")
    public  void postLikeCountTest(){

        //Given: 좋아요 수 조회 - 게시글 번호 
        int post_id = 123;

        // When : 좋아요 수 조회를 위한 세팅
        int countLike = postMapper.postLikeCount(post_id);
        

        // Then : 예상 좋아요 수와 조회되는 좋아요 수 비교
        int dataLikeCount = 10;
        assertEquals(dataLikeCount, countLike, "좋아요 수가 일치하지 않습니다.");

    }

    @Test
    @DisplayName("P06: id로 좋아요 존재 확인")
    public  void LikeExistsTest(){

        //Given: 좋아요 조회 - 게시글 번호 + 사용자 번호
        UserActivityRequestDTO like = new UserActivityRequestDTO();
        like.setMember_id(1);
        like.setPost_id(123);
        
        
        // When : userActivity 테이블에 존재 확인
        boolean commitLike = userActivityMapper.commitLike(like);
        Assertions.assertEquals( true, commitLike, "좋아요가 테이블에 있습니다.");


        // Then : 좋아요 있음 Exists : true
        boolean existsLike = true;
        assertEquals(existsLike , commitLike, "좋아요가 테이블에 있지 않습니다.");

    }


    @Test
    @DisplayName("P07: UserActivity 테이블 Insert ")
    public  void LikeACInsertTest(){

        //Given: 좋아요 조회 - 게시글 번호 + 사용자 번호
        UserActivityRequestDTO likeAC = new UserActivityRequestDTO();
        likeAC.setMember_id(1);
        likeAC.setPost_id(123);
        likeAC.setUser_activity("like");
        
        // When : UserActivity 테이블로 등록 
        int likeRow = userActivityMapper.likeACInsert(likeAC);
        Assertions.assertEquals(1, likeRow, "좋아요가 테이블에 등록 되었습니다.");

        // Then : UserActivity 테이블에서 사용자 정보 조회 및 검증
        boolean commitLike = userActivityMapper.commitLike(likeAC);     
        Assertions.assertEquals( true, commitLike, "좋아요가 테이블에 있습니다.");

        // Then : 좋아요 있음 Exists : true
        boolean existsLike = true;
        assertEquals(existsLike , commitLike, "좋아요가 테이블에 있지 않습니다.");

    }

    @Test
    @DisplayName("P08: UserActivity 테이블 delete ")
    public  void LikeACDeleteTest(){

        //Given: 좋아요 조회 - 게시글 번호 + 사용자 번호
        UserActivityRequestDTO likeAC = new UserActivityRequestDTO();
        likeAC.setMember_id(1);
        likeAC.setPost_id(123);
       
        // When : UserActivity 테이블에서 삭제 
        int likeRow = userActivityMapper.likeACDelete(likeAC);
        Assertions.assertEquals(1, likeRow, "좋아요가 테이블에서 삭제 되었습니다.");

        // Then : UserActivity 테이블에서 사용자 정보 조회 및 검증
        boolean commitLike = userActivityMapper.commitLike(likeAC);     
        Assertions.assertEquals( false, commitLike, "좋아요가 테이블에 없습니다.");

        // Then : 좋아요  Exists : false
        boolean existsLike = false;
        assertEquals(existsLike , commitLike, "좋아요가 테이블에 있습니다.");

    }
    
    @Test
    @DisplayName("P09: like만 불러와서 +1 ")
    public  void postLikeCountAddTest(){

        //Given :  좋아요 수 가져올 게시글 번호 세팅
        PostRequestDTO request = new PostRequestDTO();
        request.setPost_id(123);
        
        //When : 좋아요 수 가져오면  +1 
        int likeSum = userActivityMapper.likeCount(request.getPost_id());
        likeSum += 1;
        request.setPost_like_counts(likeSum);       
        
        // update 가 정상적으로 되면 1 아니면 0
        int likeCountAdd = userActivityMapper.postLikeCountAdd(request);

        // Then :  증가 했는지 확인
        int likeSumAfter = userActivityMapper.likeCount(request.getPost_id());
        
        Assertions.assertEquals(likeSum, likeSumAfter , "+1 증가하였습니다.");
        Assertions.assertEquals(1, likeCountAdd, "Update가 정상적으로 이루어졌는지 확인");
      
    }


    @Test
    @DisplayName("P010: like만 불러와서 -1 ")
    public  void postLikeCountMinusTest(){

        //Given :  좋아요 수 가져올 게시글 번호 세팅
        PostRequestDTO request = new PostRequestDTO();
         request.setPost_id(123);
        
        //When : 좋아요 수 가져오면  -1 
        int likeSum = userActivityMapper.likeCount(request.getPost_id());
        likeSum -= 1;
        request.setPost_like_counts(likeSum);

        // update 가 정상적으로 되면 1 아니면 0
        int likeCountAdd = userActivityMapper.postLikeCountMinus(request);

        // Then :  감소 했는지 확인
        int likeSumAfter = userActivityMapper.likeCount(request.getPost_id());
        
        Assertions.assertEquals(likeSum, likeSumAfter , "-1 감소 하였습니다.");
        Assertions.assertEquals(1, likeCountAdd, "Update가 정상적으로 이루어졌는지 확인");
        
    }


    @Test
    @DisplayName("P011: 전체 게시글 조회 ")
    public  void postAllSelectTest(){

        //Given : 전체글 페이지 세팅
        int size = 10;
        int offset =  1;
         
        //When : 전체 게시글 조회
        List<PostResponseDTO> postAll = postMapper.postAllSelect(size, offset);

        // List 가 비어있는지 확인
        Assertions.assertFalse(postAll.isEmpty(), "게시글 조회에 실패했습니다.");
    
        // Then: 게시글 정보 검증
        PostResponseDTO first = postAll.get(0);
    
        // 게시글 확인
        Assertions.assertEquals("게시글 제목 예시", first.getPost_title(), "게시글 제목이 조회 되어야 합니다.");
        Assertions.assertEquals("축구", first.getPost_sport(), "운동 종목이 조회 되어야 합니다.");
        Assertions.assertEquals(1, first.getMember_id(), "회원 id가 조회 되어야 합니다.");
            
    }

    @Test
    @DisplayName("P012: 상세 게시글 조회 ")
    public  void postDetailSelectTest(){

        //Given : 상세 게시글 조회를 위한 세팅
        PostRequestDTO request = new PostRequestDTO();
        request.setPost_id(123);

        
        //When : 상세 게시글 검색
        List<PostResponseDTO> postDetail = postMapper.postDetailSelect(request);

        // List 가 비어있는지 확인
        Assertions.assertFalse(postDetail.isEmpty(), "게시글 조회에 실패했습니다.");
        assertNotNull(postDetail);

        // Then: 게시글 정보 검증
        PostResponseDTO first = postDetail.get(0);
        
        // 댓글 가지고 오나 확인
        assertEquals(2, postDetail.get(0).getComments().size());

        // 게시글 확인
        Assertions.assertEquals("게시글 제목 예시", first.getPost_title(), "게시글 제목이 조회 되어야 합니다.");
        Assertions.assertEquals("축구", first.getPost_sport(), "운동 종목이 조회 되어야 합니다.");
        Assertions.assertEquals(1, first.getMember_id(), "회원 id가 조회 되어야 합니다.");
         
    }



    @Test
    @DisplayName("P013: UserActivity 테이블 Insert ")
    public  void ViewACInsertTest(){

        //Given: 조회수 조회 - 게시글 번호 + 사용자 번호
        UserActivityRequestDTO viewAC = new UserActivityRequestDTO();
        viewAC.setMember_id(1);
        viewAC.setPost_id(123);
        viewAC.setUser_activity("view");
        
        // When : UserActivity 테이블 등록       
        int viewRow = userActivityMapper.postViewInsert(viewAC);
        Assertions.assertEquals(1, viewRow, "조회가 테이블에 등록 되었습니다.");

        // Then : UserActivity 테이블에서 사용자 정보 조회 및 검증
        boolean commitView = userActivityMapper.commitView(viewAC);     
        Assertions.assertEquals( true, commitView, "UserActivity 테이블에 있습니다.");

        // Then : 조회 목록 있음 Exists : true
        boolean existsView = true;
        assertEquals(existsView , commitView ,"조회 목록이 테이블에 있지 않습니다.");

    }



    @Test
    @DisplayName("P014: Views만 불러와서 +1 ")
    public  void postViewCountAddTest(){

        //Given :  조회수 가져올 게시글 번호 세팅
        PostRequestDTO request = new PostRequestDTO();
        request.setPost_id(123);
        
        //When : 조회수 가져오면  +1 
        int viewSum = userActivityMapper.viewCount(request.getPost_id());
        viewSum += 1;
        request.setPost_views(viewSum);   

        // update 가 정상적으로 되면 1 아니면 0
        int viewCountAdd = userActivityMapper.postViewCountAdd(request);

        // Then :  증가 했는지 다시 호출
        int viewSumAfter = userActivityMapper.viewCount(request.getPost_id());
        
        Assertions.assertEquals(viewSum, viewSumAfter , "+1 증가하였습니다.");
        Assertions.assertEquals(1, viewCountAdd, "Update가 정상적으로 이루어졌습니다.");
      
    }





}




