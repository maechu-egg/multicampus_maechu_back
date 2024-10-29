package com.multipjt.multi_pjt.community.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.multipjt.multi_pjt.community.domain.comments.CommentRequestDTO;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.service.CommentService;


import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/community/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    // 댓글 저장
    @PostMapping("/saveComment")
    public ResponseEntity<Void> saveComment(@RequestBody CommentRequestDTO cdto) {
        System.out.println("Controller endpoint : /community/commnet/saveComment");
        System.out.println("cdto - " + cdto);

        commentService.createComment(cdto);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 댓글 list
    @GetMapping("/getComment/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> commentList(@PathVariable("postId") Integer postId) {
        System.out.println("Controller client endpoint : /community/comment/getComment/{postId}" + postId);
        System.out.println("postId - " + postId);
        
        Map<String, Integer> map = new HashMap<>();
        map.put("post_id" , postId);

        List<CommentResponseDTO> list = commentService.postDetailComment(map);

        System.out.println("result size = " + list.size());
        
        return new ResponseEntity<List<CommentResponseDTO>>(list, HttpStatus.OK);

    }
    
    // 댓글 삭제
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> commentDelete(@PathVariable("commentId") Integer commentId,
                                                @RequestBody CommentRequestDTO request                                          
    ){
      
        System.out.println("Controller client endpoint : /community/comment/delete/{commentId} : " + commentId);
        System.out.println("commentId : " + commentId);
        System.out.println("memberId : " + request.getMember_id());

        Integer memberId = request.getMember_id();
        
        Map<String, Integer> map = new HashMap<>();
        map.put("comments_id", commentId);
        map.put("member_id", memberId);
        
        commentService.commentDelete(map);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}