package com.multipjt.multi_pjt.community.ctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
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

        // 전체 페이지 조회
        @GetMapping("/posts")
        public ResponseEntity<Map<String, Object>> Posts(
                                            @RequestParam(name="page", defaultValue = "1") int page,
                                            @RequestParam(name="size", defaultValue = "10") int size,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ){


            Map<String, Object> response = new HashMap<>();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            }
            System.out.println("Controller - All posts 조회");
                    
            // 전체 페이지 조회 - 로그인 안한 사용자 
            List<PostResponseDTO> allList = postService.getAllPagePosts(page, size);
            int totalpage = postService.countPosts();
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
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ) {
            Map<String, Object> response = new HashMap<>();

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
                int totalpage = postService.countPosts();
                System.out.println("Controller - 검색 기능 " + searchList);
                System.out.println("totalpage = " + totalpage);

            
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
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
        ){

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            }
          
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
        public ResponseEntity<String> postUpdate(
                                            @RequestBody  PostRequestDTO pdto, 
                                            @PathVariable("postId") int postId,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader           
                                            
         ){

            System.out.println("controller - client end point : /posts/effortpost");
            System.out.println("pdto" + pdto);
            ResponseEntity<String> response = null;  
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
             
                pdto.setMember_id(userId);
                
                pdto.setPost_id(postId);
        
                // 글 수정 Service
                int result = postService.postUpdate(pdto);   
            
                if (result == 1) {
                    response = ResponseEntity.ok("Controller - Post updat 성공.");
                } else {
                    response = ResponseEntity.badRequest().body("Controller - Post updat 실패.");
                }
            } else{
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            return  response;
         }
         
         
        // 상세 페이지 조회 
        @GetMapping("/posts/{postId}/detail")
        public ResponseEntity<PostResponseDTO> postView(
                                                @PathVariable("postId") Integer postId,
                                                // @RequestBody PostRequestDTO request,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader   ) {
            
                PostResponseDTO postdetail = null;
                ResponseEntity<PostResponseDTO> response = null; 

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);
                                                        
                System.out.println("Controller - client end point : /posts/{postId}/detail");
                System.out.println("post id - " + postId);
            
                
                Map<String, Integer> map = new HashMap<>();
                map.put("post_id", postId);
                map.put("member_id", userId);
                
                
                // 조회수 &  UserActivity 테이블에 넣기 
                userActivityService.viewInAndUp(map);
                
                // 좋아요 상태 true false 값 넘기기
                boolean likestaus = postService.postDetailLike(map);
                System.out.println("likeStatus" + likestaus);
                                                        
                
                //  상세 페이지 조회 
                postdetail = postService.postDetail(map);       
                postdetail.setLikeStatus(likestaus);

                response = new ResponseEntity<>(postdetail, HttpStatus.OK);

            } else {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Error-Message", "로그인이 필요합니다.");
                
                response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
            }
            
            
            return response;
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
        

        @GetMapping("/posts/searchKeyword")
        public ResponseEntity<List<PostResponseDTO>> searchKeyword(
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,    
                                                PostRequestDTO pdto) {
                System.out.println("Controller - posts/searchKeyword");

                List<PostResponseDTO> list = null;
                Map<String, Object> map = new HashMap<>();

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
                System.out.println("userId : " + userId);

                map.put("post_up_sport", pdto.getPost_up_sport());
                map.put("post_sport", pdto.getPost_sport());
                map.put("post_sports_keyword", pdto.getPost_sports_keyword());

                postService.searchKeyword(map);
            
            
                

            }


            return ResponseEntity.ok(list);

        }
        
}
