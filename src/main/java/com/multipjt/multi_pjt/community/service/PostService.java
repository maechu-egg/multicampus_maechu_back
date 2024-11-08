package com.multipjt.multi_pjt.community.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;

import com.multipjt.multi_pjt.community.dao.PostMapper;
import com.multipjt.multi_pjt.community.dao.UserActivityMapper;
import com.multipjt.multi_pjt.community.domain.comments.CommentResponseDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostRequestDTO;
import com.multipjt.multi_pjt.community.domain.posts.PostResponseDTO;


@Service
public class PostService {
    
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserActivityMapper userActivityMapper;

    @Autowired
    private UserActivityRecordMapper userActivityRecordMapper;

    // 전체 페이지 
    public List<PostResponseDTO> getAllPagePosts(Map<String, Object> map){
        System.out.println("service : getAllPagePosts 조회");
        System.out.println(map);
        
        
        List<PostResponseDTO>  list = postMapper.postAllSelect(map);
        System.out.println("service   list - " + list );
        return list;
    }


    // 전체 페이지 수
    public int countPosts(Map<String, Object> map){
        System.out.println("service : countPosts 조회");
        int total = postMapper.countPosts(map);

        return total;
    }

    // 검색 기록 
    public List<PostResponseDTO> postSelectTCH(Map<String, Object> map){
        System.out.println("service : post_sports 조회");
        int page = (int)map.get("page");
        int size = (int)map.get("size");
        System.out.println("page : " + page);
        System.out.println("size : " + size);
        System.out.println("keyword : " + map.get("keyword") );
        int offset = (page - 1) * size;
        map.put("offset", offset);

        List<PostResponseDTO> searchList = postMapper.postSelectTCH(map);
        System.out.println("searchList" + searchList);

        
        return searchList;
    }

    // 게시글 등록                              
    public int postInsert(PostRequestDTO pdto,Map<String, Object> map){
        System.out.println("service - postInsert");
        int result = postMapper.postInsert(pdto);

            
        userActivityRecordMapper.insertActivityAndUpdatePoints(map);



        if(result == 1){
            System.out.println("service : 게시글이 성공적으로 등록되었습니다.");
        }else{
            System.out.println("service : 0 : 게시글 등록에 실패했습니다. ");
        }

        return result;
    }
    

    // 게시글 수정
    public int postUpdate(PostRequestDTO pdto){
    
        System.out.println("service : postUpdate");
        return postMapper.postUpdate(pdto);
    
    }

    // 업데이트 게시글 조회
    public PostResponseDTO updatePostResult(PostRequestDTO pdto){
        System.out.println("sevice : result - postupdate - select");

        return postMapper.updatePostResult(pdto);
    }

    // 게시글 삭제
    public boolean postDelete(Map<String, Integer> map){

        int result = 0;

        result = postMapper.postDelete(map);

        if(result >= 1){
            return true;
        }else{
            return false;
        }
    }



    // 상세 페이지 조회 
    public PostResponseDTO postDetail(Map<String, Object> map){
        System.out.println(" service postDetail - "   );
        System.out.println("map : " + map);
        // 게시글 정보
        PostResponseDTO result = postMapper.postDetailSelect(map);

        // 댓글 정보
        List<CommentResponseDTO> list = postMapper.postDetailComment(map);
        
        result.setComments(list);
        
        return result;
    }

    // 상세 페이지 - 좋아요 / 싫어요 상태
    public Map<String, Object> postDetailLike(Map<String, Integer> map){
        
        Map<String,Object> status = new HashMap<>();
        
        // 좋아요 정보
        boolean like = userActivityMapper.commitLike(map);
        System.out.println("좋아요 상태 1. true있다 / 없다. false " + like);
        status.put("likeStatus", like);
        
        // 싫어요 정보 
        boolean unlike = userActivityMapper.commitUnLike(map);
        System.out.println("싫어요 상태 1. true있다 / 없다. false " + unlike);
        status.put("unlikeStatus", unlike);

        return status;

    }

    // new 회원 / 일주일 이상 활동이 없는 회원인지 판단 
    public int isNewMember(int member_id){

        return postMapper.isNewMember(member_id);
    }

    // new 회원 / 일주일 이상 활동이 없는 회원 추천 리스트
    public List<PostResponseDTO> newMemberRCPost(int member_id){
        return postMapper.newMemberRCPost(member_id);
    }

    // 활동 중인 회원 추천
    public List<PostResponseDTO> exMemberData(int member_id){
        System.out.println("service - exMemberData : member_id : " + member_id);
        // 운동 종목, 키워드  추출 하기
        List<PostResponseDTO> searchKey = postMapper.exMemberData(member_id);
        System.err.println("list size" + searchKey.size());
          
        List<PostResponseDTO> recommendPost = new ArrayList<>();
        if(searchKey.size() > 0){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(0).getPost_sport(), searchKey.get(0).getPost_sports_keyword(), 3 ));
        }
        if(searchKey.size() > 1){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(1).getPost_sport(), searchKey.get(1).getPost_sports_keyword(), 1 ));
        }
        if(searchKey.size() > 2){
            recommendPost.addAll(postMapper.findRCPosts(
                searchKey.get(2).getPost_sport(), searchKey.get(2).getPost_sports_keyword(), 1 ));
        }
        
        return recommendPost;
    }

    // 비회원 추천 
    public List<PostResponseDTO> nonMemberRCPost(){

        return postMapper.nonMemberRCPost();
    }


    // 키워드 검색 
    public Map<String, Object> searchKeyword(Map<String, Object> map){
        System.out.println("service - searchKeyword");

        List<PostResponseDTO> list = null;
        list = postMapper.searchKeyword(map);

        System.out.println("list : " + list);


        Map<String, Object> result = new HashMap<>();

        if(list != null){
            result.put("result", true) ;
            result.put("list", list);

        }else if(list == null){
            result.put("result", false);
            result.put("list", new ArrayList<>());
        }


        return result;
    }

}
