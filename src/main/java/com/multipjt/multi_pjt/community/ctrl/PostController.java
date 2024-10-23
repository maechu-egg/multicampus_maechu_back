package com.multipjt.multi_pjt.community.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;
import com.multipjt.multi_pjt.community.service.PostService;



@RestController
@RequestMapping("/community")
public class PostController {


    @Autowired
    private PostService postservice;


    // 전체 페이지 조회
    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> Posts(
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size
    ){
        
        List<PostResponseDTO> allList = postservice.getAllPagePosts(page, size);
        int totalpage = postservice.countPosts();

        // react로 넘김

        Map<String, Object> response = new HashMap<>();
        response.put("posts", allList);
        response.put("totalpage", totalpage);
        response.put("totalPages", (int)Math.ceil((double) totalpage/ size)); // 페이지수 계산
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }   

    
    
}
