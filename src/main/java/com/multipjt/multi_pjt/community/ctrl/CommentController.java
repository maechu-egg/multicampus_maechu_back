package com.multipjt.multi_pjt.community.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.service.CommentService;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/community/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 댓글 저장
    @PostMapping("/saveComment")
    public ResponseEntity< Map<String, Object>> saveComment(@RequestBody CommentRequestDTO cdto,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        
        System.out.println("Controller endpoint : /community/commnet/saveComment");
        System.out.println("cdto - " + cdto);

        Map<String, Object> response = new HashMap<>();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            System.out.println("userId : " + userId);
         
            cdto.setMember_id(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("activityType", "comment");
            map.put("memberId", userId); 
            response = commentService.createComment(cdto, map);

            if(response.get("result") != null && (boolean)response.get("result")){
                response.put("message", "댓글이 성공적으로 추가되었습니다.");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }else{
                response.put("message", "댓글 추가가 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            
        }else{
            response.put("message", "로그인 한 사용자만 글쓰기가 가능합니다. ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
    }

    // 댓글 list
    @GetMapping("/getComment/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> commentList(@PathVariable("postId") Integer postId,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("Controller client endpoint : /community/comment/getComment/{postId}" + postId);
        System.out.println("postId - " + postId);
        List<CommentResponseDTO> list = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            System.out.println("userId : " + userId);
        
            Map<String, Object> map = new HashMap<>();
            map.put("post_id" , postId);

            list = commentService.postDetailComment(map);

  
            System.out.println("result size = " + list.size());

        }
        return new ResponseEntity<List<CommentResponseDTO>>(list, HttpStatus.OK);

    }
    
    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Map<String, Object>> commentDelete(@PathVariable("commentId") int commentId,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
                                                                                  
    ){
        ResponseEntity<Map<String, Object>> response = null;    
        Map<String, Object> result = new HashMap<>();              
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출 (int형으로 변경)
            System.out.println("userId : " + userId);
      
            try{
                System.out.println("Controller client endpoint : /community/comment/delete/{commentId} : " + commentId);
                System.out.println("commentId : " + commentId);
                System.out.println("memberId : " + userId);
                
                Map<String, Integer> map = new HashMap<>();
                map.put("comments_id", commentId);
                map.put("member_id",userId);
                // map.put("post_id", cdto.getPost_id());
                
                result = commentService.commentDelete(map);
                if(result.get("result") != null && (boolean)result.get("result")){         
                    response = ResponseEntity.ok(result);
                }else if(result.get("Extable") != null && !(boolean)result.get("Extable")){
                    response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }else {
                    response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
                
            }catch(Exception e){

                System.err.println("오류 발생: " + e.getMessage());
                e.printStackTrace();
                result.put("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);

            }
        }else{
            result.put("message", "로그인이 필요합니다.");
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return response;
    }
}
