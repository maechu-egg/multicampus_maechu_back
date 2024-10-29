package com.multipjt.multi_pjt.community.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.community.service.PostService;
import com.multipjt.multi_pjt.community.service.UserActivityService;



@RestController
@RequestMapping("/community")
public class PostController {


        @Autowired
        private PostService postService;

        @Autowired
        private UserActivityService userActivityService;

        // 전체 페이지 조회
        @GetMapping("/posts")
        public ResponseEntity<Map<String, Object>> Posts(
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ){
            System.out.println("Controller - All posts 조회");
                    
            // 전체 페이지 조회 - 로그인 안한 사용자 
            List<PostResponseDTO> allList = postService.getAllPagePosts(page, size);
            int totalpage = postService.countPosts();
            System.out.println("Controller - 전체 게시글 조회 " + allList);
            System.out.println("totalpage = " + totalpage);
            


            Map<String, Object> response = new HashMap<>();
            response.put("posts", allList);
            response.put("totalpage", totalpage);
            response.put("totalPages", (int)Math.ceil((double) totalpage/ size)); // 페이지수 계산
            response.put("currentPage", page);
        
            response.put("message", "전체 페이지 조회");

            return ResponseEntity.ok(response);
        }   


        // 검색 기능
        @GetMapping("/posts/search")
        public ResponseEntity<Map<String, Object>> PostSearch(@RequestParam("keyword") String keyword,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size
        ) {
            
            
            System.out.println("controller - posts - search : " + keyword );

            Map<String, Object> map = new HashMap<>();
            map.put("keyword" , keyword);
            map.put("size"  , size);
            map.put("page", page);

            // 페이지 조회
            List<PostResponseDTO> searchList = postService.postSelectTCH(map);
            int totalpage = postService.countPosts();
            System.out.println("Controller - 검색 기능 " + searchList);
            System.out.println("totalpage = " + totalpage);

            Map<String, Object> response = new HashMap<>();
            response.put("posts", searchList);
            response.put("totalpage", totalpage);
            response.put("totalPages", (int)Math.ceil((double) totalpage/ size)); // 페이지수 계산
            response.put("currentPage", page);
        
            response.put("message", keyword + " 페이지 조회");
            return ResponseEntity.ok(response);
        }
        


        // 운동 종목 별 조회 
        @GetMapping("/posts/{post_sport}")
        public ResponseEntity<Map<String, Object>> PostSport(
                                            @PathVariable("post_sport") String sport,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size
        ){
          
            System.out.println("page : " + page + " / size : " + size);
            Map<String, Object> map = new HashMap<>();
            map.put("post_sport", "헬스");
            map.put("page", 1);
            map.put("size", 10);
            
            // 운동 종목 별 
            List<PostResponseDTO> sportList = postService.postSelectSport(map);
            int totalpage = postService.countPosts();
 
            Map<String, Object> response = new HashMap<>();
            response.put("sportpost", sportList);
            response.put("totalpage", totalpage);
            response.put("totalPages", (int)Math.ceil((double) totalpage/ size)); // 페이지수 계산
            response.put("currentPage", page);
            
            response.put("message", "운동 종목 별 페이지 : " + sport);

            return ResponseEntity.ok(response);
        }   


        // 글 작성 
        @PostMapping("/posts/effortpost")
        public ResponseEntity<Void> postInsert(@RequestBody  PostRequestDTO pdto){
        
            // 로그인 상태       
            System.out.println("controller - client end point : /posts/effortpost");
            System.out.println("pdto" + pdto);

            // 글 등록 Service
            postService.postInsert(pdto);
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }   
 
    
         // 글 수정 
        @PutMapping("/posts/{postId}/update")
        public ResponseEntity<String> postUpdate(
                                            @RequestBody  PostRequestDTO pdto, 
                                            @PathVariable("postId") int postId           
                                            
         ){
            System.out.println("controller - client end point : /posts/effortpost");
            System.out.println("pdto" + pdto);
            
            pdto.setPost_id(postId);
    
            // 글 수정 Service
            int result = postService.postUpdate(pdto);     
            
            if (result == 1) {
                return ResponseEntity.ok("Controller - Post updat 성공.");
            } else {
                return ResponseEntity.badRequest().body("Controller - Post updat 실패.");
            }
         }
         
         
        // 상세 페이지 조회 
        @GetMapping("/posts/{postId}/detail")
        public ResponseEntity<PostResponseDTO> postView(
                                                @PathVariable("postId") Integer postId,
                                                @RequestBody PostRequestDTO request) {
            
            System.out.println("Controller - client end point : /posts/{postId}/detail");
            System.out.println("post id - " + postId);
            System.out.println("member_id - " + request.getMember_id());
            
            Map<String, Integer> map = new HashMap<>();
            map.put("post_id", postId);
            map.put("member_id", request.getMember_id());
            
            
            // 1.  UserActivity 테이블에 넣기 
            userActivityService.viewInsert(map);
            
            // 2. 조회수 증가. 
            userActivityService.viewCount(map);
            
            // 3.좋아요 상태 true false 값 넘기기
            boolean likestaus = postService.postDetailLike(map);
            System.out.println("likeStatus" + likestaus);
                                                    
            
            // 3. 상세 페이지 조회 
            PostResponseDTO postdetail = postService.postDetail(map);       
 
            postdetail.setLikeStatus(likestaus);
            
            return new ResponseEntity<>(postdetail, HttpStatus.OK);
        
        }
         

        // 비회원 추천
        @GetMapping("/posts/guest")
        public ResponseEntity<List<PostResponseDTO>> nonMemberRCPost() {
            System.out.println("Controller - posts/guest : 비회원 추천 ");
            List<PostResponseDTO> result = postService.nonMemberRCPost();
            
            return ResponseEntity.ok(result);
        }
            


        // 회원 추천 
        @GetMapping("/posts/userRC")
        public ResponseEntity<List<PostResponseDTO>> userRecomend(@RequestParam int userId) {
            System.out.println("Controller - posts/userRC 회원 추천 ");
            
            // 신규회원 / 활동없는 회원일 경우 1 이상 반환
            int isNew = postService.isNewMember(userId);
            
            List<PostResponseDTO> list = null;
            if(isNew >= 1 ){
                // 신규 회원 / 활동 없는 회원일 경우 
                list = postService.newMemberRCPost(userId);
            }else{
                list = postService.exMemberData(userId);
            }

            return ResponseEntity.ok(list);
        }
        


}
