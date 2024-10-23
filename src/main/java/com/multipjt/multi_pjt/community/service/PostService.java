package com.multipjt.multi_pjt.community.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;


@Service
public class PostService {
    
    @Autowired
    private PostMapper posstMapper;


    public List<PostResponseDTO> getAllPagePosts(int page, int size){
        int offset = (page - 1 ) * size ;
        
        List<PostResponseDTO>  list = posstMapper.postAllSelect(size, offset);
        
        return list;


    }

    // 전체 페이지 수
    public int countPosts(){
        int total = posstMapper.countPosts();

        return total;
    }

}
