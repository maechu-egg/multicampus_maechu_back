package com.multipjt.multi_pjt.community.ctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.community.service.CommentService;
import com.multipjt.multi_pjt.community.service.PostService;
import com.multipjt.multi_pjt.community.service.UserActivityService;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;





@RestController
@RequestMapping("/community")
public class PostController {

        private static final String POST_IMAGE_REPO = "C:\\maechu\\community\\post_image";

        @Autowired
        private PostService postService;

        @Autowired
        private UserActivityService userActivityService;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Autowired
        private CommentService commentService;

        // 전체 페이지 조회
        @GetMapping("/posts")
        public ResponseEntity<Map<String, Object>> Posts(
                                            @RequestParam(name="page", defaultValue = "1") int page,
                                            @RequestParam(name="size", defaultValue = "10") int size,
                                            @RequestParam(name="post_up_sport", required = false) String post_up_sport,
                                            @RequestParam(name="post_sport", required = false) String post_sport,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ){

            System.out.println("Controller - All posts 조회");
            Map<String, Object> response = new HashMap<>();
            System.out.println("post_up_sport : " + post_up_sport);
            System.out.println("post_sport" + post_sport);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            }
           
                    
            // 전체 페이지 조회 - 로그인 안한 사용자 
            Map<String, Object> map = new HashMap<>();
            int offset = (page - 1 ) * size ;
            map.put("offset", offset);
            map.put("size", size);
            map.put("post_up_sport", post_up_sport);
            map.put("post_sport", post_sport);
            System.out.println("map 입력후 ");
            System.out.println("post_up_sport : " + post_up_sport);
            System.out.println("post_sport" + post_sport);
            List<PostResponseDTO> allList = postService.getAllPagePosts(map);
           
            int totalpage = postService.countPosts(map);
            System.out.println("Controller - 전체 게시글 조회 " + allList);
            System.out.println("totalpage = " + totalpage);
            
            
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
                                                                @RequestParam(name="page" ,defaultValue = "1") int page,
                                                                @RequestParam(name="size" , defaultValue = "10") int size,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ) {
            Map<String, Object> response = new HashMap<>();
            int totalpage = 0;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            }
                System.out.println("controller - posts - search : " + keyword );

                Map<String, Object> map = new HashMap<>();
                map.put("keyword" , keyword);
                map.put("size"  , size);
                map.put("page", page);
                
                // 페이지 조회
                List<PostResponseDTO> searchList = postService.postSelectTCH(map);
              
                if(searchList == null || searchList.isEmpty()){
                    response.put("message" ,"검색결과가 없습니다");
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("posts", new ArrayList<>());
                  
                }else if(searchList != null) {
                    
                    totalpage = postService.countPosts(map);
                    System.out.println("Controller - 검색 기능 " + searchList);
                    System.out.println("totalpage = " + totalpage);

                
                    response.put("posts", searchList);
                    response.put("totalpage", totalpage);
                    response.put("totalPages", (int)Math.ceil((double) totalpage/ size)); // 페이지수 계산
                    response.put("currentPage", page);
                
                    response.put("message", keyword + " 페이지가 성공적으로 조회되었습니다.");
                 
                    response.put("status", HttpStatus.OK.value());

                   
                }
                HttpStatus status = (searchList == null || searchList.isEmpty()) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
                return new ResponseEntity<>(response, status);
        }
        

        // 글 작성 
        @PostMapping("/posts/effortpost")
        public ResponseEntity<String> postInsert(@ModelAttribute PostRequestDTO pdto,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles
        ){
                                        
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
                // 로그인 상태       
                System.out.println("controller - client end point : /posts/effortpost");
                System.out.println("pdto" + pdto);
                System.out.println("imageFiles - " + imageFiles);

                pdto.setMember_id(userId);
                System.out.println(pdto);
              
                Map<String, Object> map = new HashMap<>();
                map.put("activityType", "post");
                map.put("memberId", userId);
               

                if(imageFiles != null && !imageFiles.isEmpty()){
                    
                    try{

                        Path directory = Paths.get(POST_IMAGE_REPO);
                        if (!Files.exists(directory)) {
                            Files.createDirectories(directory);
                        }
                        
                        if(imageFiles.size() > 0 && !imageFiles.get(0).isEmpty()){
                            String originalFileName = imageFiles.get(0).getOriginalFilename();
                            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                            String uniqueFileName1 = UUID.randomUUID().toString() + extension;
                            String filePath1 = POST_IMAGE_REPO  + "\\" +uniqueFileName1;
                            imageFiles.get(0).transferTo(new File(filePath1));
                            pdto.setPost_img1("/images/"+uniqueFileName1);
                            System.out.println("posts image" + pdto.getPost_img1());
                        }
                        if(imageFiles.size() > 1 && !imageFiles.get(1).isEmpty()){
                            String originalFileName = imageFiles.get(1).getOriginalFilename();
                            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                            String uniqueFileName2 = UUID.randomUUID().toString() + extension;
                            String filePath2 = POST_IMAGE_REPO +"\\"+ uniqueFileName2;
                            imageFiles.get(1).transferTo(new File(filePath2));
                            pdto.setPost_img2("/images/"+uniqueFileName2);
                            System.out.println("posts image" + pdto.getPost_img2());
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 파일 업로드 중 오류가 발생했습니다.");
                    }
                    
                }

                    // 글 등록 Service
                    postService.postInsert(pdto, map);

                return  ResponseEntity.ok("글이 등록되었습니다.");
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 한 사용자만 글쓰기가 가능합니다.");
            }
            
        }   
 
    
           // 글 수정 
        @PutMapping("/posts/{postId}/update")
        public ResponseEntity<PostResponseDTO> postUpdate(
                                            @ModelAttribute  PostRequestDTO pdto, 
                                            @PathVariable("postId") int postId,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles           
                                            
         ){

            System.out.println("controller - client end point : /posts/{postId}/update");
            System.out.println("pdto" + pdto);
            ResponseEntity<PostResponseDTO> response = null; 
            PostResponseDTO upost = null;
            boolean isAuthor = false;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
             
                pdto.setMember_id(userId);
                
                pdto.setPost_id(postId);
                System.out.println("controller - pdto " + pdto);
                // 글 수정 Service
                int result = postService.postUpdate(pdto);   
                
                if (result == 1) {
                    upost = postService.updatePostResult(pdto);
                    isAuthor = (long)upost.getMember_id() == userId;
                    upost.setAuthor(isAuthor);
                    System.out.println("debug >>>>>>>>>>>>>>>>>>>>> pdto " + pdto);
                    response = new ResponseEntity<>(upost, HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else{
                HttpHeaders headers = new HttpHeaders();
                headers.add("Error-Message", "로그인이 필요합니다.");
                
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
            }
            return response;
            
         }
         
         
        // 상세 페이지 조회 
        @GetMapping("/posts/{postId}/detail")
        public ResponseEntity<PostResponseDTO> postView(
                                                @PathVariable("postId") Integer postId,
                                                // @RequestBody PostRequestDTO request,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader   ) {
            
                PostResponseDTO postdetail = null;
                ResponseEntity<PostResponseDTO> response = null; 
                Map<String, Object> status = new HashMap<>();
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
                                                        
                System.out.println("Controller - client end point : /posts/{postId}/detail");
                System.out.println("post id - " + postId);
            
                
                Map<String, Object> map = new HashMap<>();
                map.put("post_id", postId);
                map.put("member_id", userId);
                //  상세 페이지 조회 
                postdetail = postService.postDetail(map); 
                
                // 상세 게시글 작성자 - 로그인 사용자 비교
                if(postdetail == null){
                    return ResponseEntity.notFound().build();
                }
                
                boolean isAuthor = (long)postdetail.getMember_id() == userId;
                

                postdetail.setAuthor(isAuthor);
                Map<String, Integer> viewLikeMap = new HashMap<>();
                viewLikeMap.put("post_id", postId);
                viewLikeMap.put("member_id", userId);
                // 조회수 &  UserActivity 테이블에 넣기 
                userActivityService.viewInAndUp(viewLikeMap);
                  
                // 좋아요 상태 true false 값 넘기기
                status= postService.postDetailLike(viewLikeMap);
                
                // 댓글 list
                List<CommentResponseDTO> comments = commentService.postDetailComment(map);

                if (comments != null && !comments.isEmpty()) {
                        postdetail.setComments(comments);
                }

                if(status.get("likeStatus") != null && (boolean)status.get("likeStatus")){
                    postdetail.setLikeStatus(true);
                }else if(status.get("unlikeStatus") != null && (boolean)status.get("unlikeStatus")){
                    postdetail.setUnlikeStatus(true);
                }

                System.out.println("controller - postdetail : " + postdetail);   
                
                response = new ResponseEntity<>(postdetail, HttpStatus.OK);

            } else {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Error-Message", "로그인이 필요합니다.");
                
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
            }
            
            
            return response;
        }
         
        // 글 삭제
        @DeleteMapping("/posts/{postId}/delete")
        public ResponseEntity<String> postDelete( @PathVariable("postId")Integer postId ,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ){

                boolean postdelete = false;
           
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
                                                        
                System.out.println("Controller - client end point : /posts/{postId}/delete");
                System.out.println("post id - " + postId);
            
                
                Map<String, Integer> map = new HashMap<>();
                map.put("post_id", postId);
                map.put("member_id", userId);
               
                postdelete = postService.postDelete(map); 
                
               
                if(postdelete){
                    return ResponseEntity.ok("게시글 삭제가 완료되었습니다.");
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
                }

            } else {
   
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

        
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
        public ResponseEntity<List<PostResponseDTO>> userRecomend( @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader  ) {
           
           List<PostResponseDTO> list = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
           
            
                System.out.println("Controller - posts/userRC 회원 추천 ");
                
                // 신규회원 / 활동없는 회원일 경우 1 이상 반환
                int isNew = postService.isNewMember(userId);
                
                
                if(isNew >= 1 ){
                    // 신규 회원 / 활동 없는 회원일 경우 
                    list = postService.newMemberRCPost(userId);
                }else{
                    list = postService.exMemberData(userId);
                }
            }


            return ResponseEntity.ok(list);
        }
        
        // 키워드 검색
        @GetMapping("/posts/sport/{post_sports_keyword}")
        public ResponseEntity<Map<String, Object>> searchKeyword(
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,    
                                                @PathVariable("post_sports_keyword") String post_sports_keyword,
                                                // @RequestParam(name="post_up_sport") String post_up_sport,
                                                // @RequestParam(name="post_sport") String post_sport,
                                                @RequestParam(name="page", defaultValue = "1") int page,
                                                @RequestParam(name="size", defaultValue = "10") int size) {
                System.out.println("Controller - posts/searchKeyword");

                List<PostResponseDTO> list = null;
                Map<String, Object> map = new HashMap<>();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);

                // map.put("post_up_sport", post_up_sport);
                // map.put("post_sport", post_sport);
                
                int offset = (page - 1 ) * size ;
                map.put("offset", offset);
                map.put("size", size);
                map.put("post_sports_keyword", post_sports_keyword);
                

        
                map = postService.searchKeyword(map);
                if(map.get("result") != null && (boolean)map.get("result")){
                    map.put("searchList", map.get("list"));
                   
                    map.put("message", "searchKeyword 성공!");
                    map.put("status", HttpStatus.OK);
                    System.out.println("map 출력");
                    System.out.println( map.get("list"));
                    
                }else if(map.get("result") != null && !(boolean)map.get("result")){
                    map.put("message", "searchKeyword 실패!!");
                    map.put("searchList", map.get("list"));
                    map.put("status",HttpStatus.NOT_FOUND );
                }


            }


            return ResponseEntity.ok(map);

        }
 
        

    // 오운완
   @GetMapping("/posts/sport/searchToday")
   public ResponseEntity<Map<String, Object>> searchToday(
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader   
                                           ) {
           System.out.println("Controller - posts/searchKeyword");
           Map<String, Object> map = new HashMap<>();

       if (authHeader != null && authHeader.startsWith("Bearer ")) {
           String token = authHeader.substring(7); // "Bearer " 접두사 제거
           int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
           System.out.println("userId : " + userId);


   
           map = postService.searchToday();
           if(map.get("result") != null && (boolean)map.get("result")){
               map.put("searchList", map.get("list"));
              
               map.put("message", "오운완 조회 성공 !");
               map.put("status", HttpStatus.OK);
               System.out.println("map 출력");
               System.out.println( map.get("list"));
               
           }else if(map.get("result") != null && !(boolean)map.get("result")){
               map.put("message", "오운완 조회 실패!!");
               map.put("searchList", map.get("list"));
               map.put("status",HttpStatus.NOT_FOUND );
           }
       }
       return ResponseEntity.ok(map);

   }
       
    // 중고장터
   @GetMapping("/posts/sport/searchMarketplace")
   public ResponseEntity<Map<String, Object>> searchMarketplace(
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader    
                                           ) {
           System.out.println("Controller - posts/searchMarketplace");
           Map<String, Object> map = new HashMap<>();

       if (authHeader != null && authHeader.startsWith("Bearer ")) {
           String token = authHeader.substring(7); // "Bearer " 접두사 제거
           int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
           System.out.println("userId : " + userId);

   
           map = postService.searchMarketplace();
           if(map.get("result") != null && (boolean)map.get("result")){
               map.put("searchList", map.get("list"));
              
               map.put("message", "중고장터 조회 성공!");
               map.put("status", HttpStatus.OK);
               System.out.println("map 출력");
               System.out.println( map.get("list"));
               
           }else if(map.get("result") != null && !(boolean)map.get("result")){
               map.put("message", "중고장터 조회 실패!!");
               map.put("searchList", map.get("list"));
               map.put("status",HttpStatus.NOT_FOUND );
           }


       }


       return ResponseEntity.ok(map);

   }
        // 사용자 - 좋아요한 게시글 조회
        @GetMapping("/myPage/myLikePosts")
        public ResponseEntity<Map<String, Object>> myLikePosts(
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader    
        ) {
                System.out.println("Controller - posts/myPosts");
                Map<String, Object> map = new HashMap<>();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);

        
                map = postService.myLikePosts(userId);
                if(map.get("result") != null && (boolean)map.get("result")){
                    map.put("searchList", map.get("list"));
                    
                    map.put("message", "myLikePosts 조회 성공!");
                    map.put("status", HttpStatus.OK);
                    System.out.println("map 출력");
                    System.out.println( map.get("list"));
                    
                }else if(map.get("result") != null && !(boolean)map.get("result")){
                    map.put("message", "myLikePosts 조회 실패!!");
                    map.put("searchList", map.get("list"));
                    map.put("status",HttpStatus.NOT_FOUND );
                }
            }

            return ResponseEntity.ok(map);
        }

        // 사용자 본인 게시글 조회
        @GetMapping("/myPage/myPosts")
        public ResponseEntity<Map<String, Object>> myPosts(
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader    
        ) {
                System.out.println("Controller - posts/myPosts");
                Map<String, Object> map = new HashMap<>();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);

        
                map = postService.myPosts(userId);
                if(map.get("result") != null && (boolean)map.get("result")){
                    map.put("searchList", map.get("list"));
                    
                    map.put("message", "myPosts 조회 성공!");
                    map.put("status", HttpStatus.OK);
                    System.out.println("map 출력");
                    System.out.println( map.get("list"));
                    
                }else if(map.get("result") != null && !(boolean)map.get("result")){
                    map.put("message", "myPosts 조회 실패!!");
                    map.put("searchList", map.get("list"));
                    map.put("status",HttpStatus.NOT_FOUND );
                }

            }
            return ResponseEntity.ok(map);

        }

}
