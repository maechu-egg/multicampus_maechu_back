package com.multipjt.multi_pjt.community.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        // System.out.println("service   list - " + list );
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


    public int countSearchPosts(Map<String, Object> map){
        int totalpost= postMapper.countSearchPosts(map);
    
        return totalpost;
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

    public void recommendationClick(Map<String, Object> map){
        System.out.println("service - isRecommendation True");

        postMapper.recommendationClick(map);
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
    public List<PostResponseDTO> newMemberRCPost(Map<String, Object> map ){
     
        List<String> interestSport = postMapper.interestSport(map);

        List<PostResponseDTO> recPosts = new ArrayList<>();
        recPosts.clear();
        Map<String, Object> RCS = new HashMap<>();
        RCS.put("member_id", map.get("member_id"));
        RCS.put("post_up_sport", map.get("post_up_sport"));
        String post_up_sport = (String)map.get("post_up_sport");
    
        if(interestSport.isEmpty()){
            // 빈 리스트 
            return recPosts;
        }
        
        
        if(interestSport.size() == 1){
            System.out.println("size() = " + interestSport.size());
            System.out.println("관심 운동 1 - " + interestSport.get(0));
            RCS.put("post_sport", interestSport.get(0));
            RCS.put("limit", 5);
            recPosts = postMapper.newMemberRCPost(RCS);
           
        }else if(interestSport.size() == 2){
            System.out.println("size() = " + interestSport.size());
            System.out.println("관심 운동 1 - " + interestSport.get(0));
            System.out.println("관심 운동 2 - " + interestSport.get(1));
            String upPost1 = postMapper.searchUpPost(interestSport.get(0));
            String upPost2 = postMapper.searchUpPost(interestSport.get(1));
           
            if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport)){
                RCS.put("limit", 3);
                RCS.put("post_sport", interestSport.get(0));
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",2);
                RCS.put("post_sport", interestSport.get(1));
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
            }else{ 
                
                if(upPost1.equals(post_up_sport)){
                RCS.put("limit", 5);
                RCS.put("post_sport", interestSport.get(0));
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                }
                if(upPost2.equals(post_up_sport) && !upPost1.equals(upPost2)){
                RCS.put("limit", 5);
                RCS.put("post_sport", interestSport.get(1));
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                }
            }
            
        }else if(interestSport.size() == 3){
            String upPost1 = postMapper.searchUpPost(interestSport.get(0));
            String upPost2 = postMapper.searchUpPost(interestSport.get(1));
            String upPost3 = postMapper.searchUpPost(interestSport.get(2));
            String soPost1 = interestSport.get(0);
            String soPost2 = interestSport.get(1);
            String soPost3 = interestSport.get(2);
            if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                RCS.put("limit", 3);
                RCS.put("post_sport", soPost1);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",1);
                RCS.put("post_sport", soPost2);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",1);
                RCS.put("post_sport", soPost3);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
        
            }else if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport)){
                RCS.put("limit", 3);
                RCS.put("post_sport",soPost1);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",2);
                RCS.put("post_sport", soPost2);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                if (upPost3.equals(post_up_sport) && !upPost3.equals(upPost1) && !upPost3.equals(upPost2)) {
                    RCS.put("limit", 5);
                    RCS.put("post_sport", soPost3);
                    recPosts.addAll(postMapper.newMemberRCPost(RCS));
                }
         
            }else if(upPost2.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                RCS.put("limit", 3);
                RCS.put("post_sport", soPost2);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",2);
                RCS.put("post_sport", soPost3);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                if (upPost1.equals(post_up_sport) && !upPost1.equals(upPost2) && !upPost1.equals(upPost3)) {
                    RCS.put("post_sport", soPost1);
                    RCS.put("limit", 5);
                    recPosts.addAll(postMapper.newMemberRCPost(RCS));
                }
                
            }else if(upPost1.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                RCS.put("limit", 3);
                RCS.put("post_sport", soPost1);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                RCS.put("limit",2);
                RCS.put("post_sport", soPost3);
                recPosts.addAll(postMapper.newMemberRCPost(RCS));
                if (upPost2.equals(post_up_sport) && !upPost2.equals(upPost1) && !upPost2.equals(upPost3)) {
                    RCS.put("post_sport", soPost2);
                    RCS.put("limit", 5);
                    recPosts.addAll(postMapper.newMemberRCPost(RCS));
                }
                
            }else {
                    if(upPost1.equals(post_up_sport)){
                        RCS.put("limit", 5);
                        RCS.put("post_sport", soPost1);
                        recPosts.addAll(postMapper.newMemberRCPost(RCS));
                    }
                    if(upPost2.equals(post_up_sport)){
                        RCS.put("limit", 5);
                        RCS.put("post_sport", soPost2);
                        recPosts.addAll(postMapper.newMemberRCPost(RCS));
                    }
                    if(upPost3.equals(post_up_sport)){
                        RCS.put("limit", 5);
                        RCS.put("post_sport", soPost3);
                        recPosts.addAll(postMapper.newMemberRCPost(RCS));
                    }
                    
            }   
        }
        Set<PostResponseDTO> uniquePosts = new LinkedHashSet<>(recPosts);
        recPosts = new ArrayList<>(uniquePosts);
        return recPosts;
    }

    // 활동 중인 회원 추천
    public List<PostResponseDTO> exMemberData(Map<String, Object> map){
        System.out.println("service - exMemberData : member_id : " + map.get("member_id"));
        

        int member_id = (Integer)map.get("member_id");
        String post_up_sport = (String)map.get("post_up_sport");
        System.out.println("react pass - post_up_sport : " + post_up_sport);

        List<PostResponseDTO> searchKey = postMapper.exMemberData(member_id);
        System.err.println("list size : " + searchKey.size());
        System.out.println("seachKey : " + searchKey);
        String upPost1;
        String upPost2;
        String upPost3;
        String soPost1;
        String soPost2;
        String soPost3;
        Map<String, Object> next = new HashMap<>();
      
    
        List<PostResponseDTO> recommendPost = new ArrayList<>();

        // 제외
        List<Integer> notinPostId = postMapper.notinPostID(member_id);
        System.out.println(notinPostId);
        if(notinPostId != null && notinPostId.size() > 0){
            next.put("postIds", notinPostId);
        }

        if(searchKey.isEmpty()){
            // 빈 리스트 
            return recommendPost;
        }
        recommendPost.clear();
       
        if(searchKey.size() == 1){
            System.out.println("size() = " + searchKey.size());
            System.out.println("대분류 1 - " + searchKey.get(0).getPost_up_sport());
            System.out.println("소분류 1 - " + searchKey.get(0).getPost_sport());
 
            if(searchKey.get(0).getPost_up_sport().equals(post_up_sport)){
                next.put("post_sport",(String)searchKey.get(0).getPost_sport() );
                next.put("post_up_sport",(String)searchKey.get(0).getPost_up_sport() );
                next.put("limit", 6);

                recommendPost.addAll(postMapper.findRCPosts(next));
            }
        }else if(searchKey.size() == 2){
            System.out.println("size() = " + searchKey.size());
            System.out.println("대분류 1 - " + searchKey.get(0).getPost_up_sport());
            System.out.println("소분류 1 - " + searchKey.get(0).getPost_sport());
            System.out.println("대분류 2 - " + searchKey.get(1).getPost_up_sport());
            System.out.println("소분류 2 - " + searchKey.get(1).getPost_sport());
            upPost1 = (String)searchKey.get(0).getPost_up_sport();
            upPost2 = (String)searchKey.get(1).getPost_up_sport();
            soPost1 = (String)searchKey.get(0).getPost_sport();
            soPost2 = (String)searchKey.get(1).getPost_sport();
        
            if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport)){
                next.put("post_sport",soPost1 );
                next.put("post_up_sport",upPost1 );
                next.put("limit", 3);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost2 );
                next.put("limit", 2);
                recommendPost.addAll(postMapper.findRCPosts(next));
          
            }else{
                    if(upPost1.equals(post_up_sport)){
                        next.put("post_sport",soPost1 );
                        next.put("post_up_sport",upPost1 );
                        next.put("limit", 5);
                        recommendPost.addAll(postMapper.findRCPosts(next));
                    }
                    if(upPost2.equals(post_up_sport)  && !upPost2.equals(upPost1)){
                        next.put("post_sport",soPost2 );
                        next.put("post_up_sport",upPost2 );
                        next.put("limit", 5);
                        recommendPost.addAll(postMapper.findRCPosts(next));
                    }
            }
            
        }else if(searchKey.size() >= 3){
            upPost1 = (String)searchKey.get(0).getPost_up_sport();
            upPost2 = (String)searchKey.get(1).getPost_up_sport();
            upPost3 = (String)searchKey.get(2).getPost_up_sport();
            soPost1 = (String)searchKey.get(0).getPost_sport();
            soPost2 = (String)searchKey.get(1).getPost_sport();
            soPost3 = (String)searchKey.get(2).getPost_sport();
        
            if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                next.put("post_sport",soPost1 );
                next.put("post_up_sport",upPost1 );
                next.put("limit", 3);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost2 );
                next.put("limit", 1);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost3 );
                next.put("limit", 1);
                recommendPost.addAll(postMapper.findRCPosts(next));
             
            }else if(upPost1.equals(post_up_sport) && upPost2.equals(post_up_sport)){
                next.put("post_sport",soPost1 );
                next.put("post_up_sport",upPost1 );
                next.put("limit", 3);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost2 );
                next.put("limit", 2);
                recommendPost.addAll(postMapper.findRCPosts(next));
                if (upPost3.equals(post_up_sport) && !upPost3.equals(upPost1) && !upPost3.equals(upPost2)) {
                    next.put("limit", 5);
                    next.put("post_sport", soPost3);
                    recommendPost.addAll(postMapper.newMemberRCPost(next));
                }
            }else if(upPost2.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                next.put("post_sport",soPost2 );
                next.put("post_up_sport",upPost2 );
                next.put("limit", 3);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost3 );
                next.put("limit", 2);
                recommendPost.addAll(postMapper.findRCPosts(next));
                if (upPost1.equals(post_up_sport) && !upPost1.equals(upPost2) && !upPost3.equals(upPost3)) {
                    next.put("limit", 5);
                    next.put("post_sport", soPost1);
                    recommendPost.addAll(postMapper.newMemberRCPost(next));
                }
            }else if(upPost1.equals(post_up_sport) && upPost3.equals(post_up_sport)){
                next.put("post_sport",soPost1 );
                next.put("post_up_sport",upPost1 );
                next.put("limit", 3);
                recommendPost.addAll(postMapper.findRCPosts(next));
                next.put("post_sport",soPost3 );
                next.put("limit", 2);
                recommendPost.addAll(postMapper.findRCPosts(next));
                if (upPost2.equals(post_up_sport) && !upPost2.equals(upPost1) && !upPost2.equals(upPost3)) {
                    next.put("limit", 5);
                    next.put("post_sport", soPost2);
                    recommendPost.addAll(postMapper.newMemberRCPost(next));
                }
            }else { 
                    if(upPost1.equals(post_up_sport)){
                        next.put("post_sport",soPost1 );
                        next.put("post_up_sport",upPost1 );
                        next.put("limit", 5);
                        recommendPost.addAll(postMapper.findRCPosts(next));


                    }
                    if(upPost2.equals(post_up_sport) && !upPost2.equals(upPost1) && !upPost2.equals(upPost3) ){
                        next.put("post_sport",soPost2 );
                        next.put("post_up_sport",upPost2 );
                        next.put("limit", 5);
                        recommendPost.addAll(postMapper.findRCPosts(next));

                    }
                    if(upPost3.equals(post_up_sport) && !upPost3.equals(upPost2) && !upPost3.equals(upPost1) ){
                        next.put("post_sport",soPost3 );
                        next.put("post_up_sport",upPost3 );
                        next.put("limit", 5);
                        recommendPost.addAll(postMapper.findRCPosts(next));

                    }
            }
            Set<PostResponseDTO> uniquePosts = new LinkedHashSet<>(recommendPost);
            recommendPost = new ArrayList<>(uniquePosts);
            System.out.println("recommendPost : " + recommendPost);
        }
    
        return recommendPost;
    }

    // 비회원 추천 
    public List<PostResponseDTO> nonMemberRCPost(){

        return postMapper.nonMemberRCPost();
    }
    // 추천 테이블에 넣기
    public String insertRecommendation(Map<String, Object> map){

       int result =  postMapper.insertRec(map);

       if(result == 1 ){
            return "중복 없음 - 성공";
       }else{
            return "중복 있음 - 실패";
       }

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
            int totalpage = postMapper.countKeyword(map);
            result.put("totalpage", totalpage);
        
        }else if(list == null){
            result.put("result", false);
            result.put("list", new ArrayList<>());
        }


        return result;
    }

        // 오운완 검색 
        public Map<String, Object> searchToday(){
            System.out.println("service - searchToday");

            List<PostResponseDTO> list = null;
            list = postMapper.searchToday();

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


        // 중고장터 검색
        public Map<String, Object> searchMarketplace(){
            System.out.println("service - searchToday");

            List<PostResponseDTO> list = null;
            list = postMapper.searchMarketplace();

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

        // 사용자 본인 게시글 검색
        public Map<String, Object> myPosts(int member_id){
            System.out.println("service - searchToday");

            List<PostResponseDTO> list = null;
            list = postMapper.myPosts(member_id);

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


        // 사용자 - 좋아요한 게시글 검색
        public Map<String, Object> myLikePosts(int member_id){
            System.out.println("service - searchToday");

            List<PostResponseDTO> list = null;
            list = postMapper.myLikePosts(member_id);

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
