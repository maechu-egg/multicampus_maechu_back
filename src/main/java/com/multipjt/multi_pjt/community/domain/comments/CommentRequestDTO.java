package com.multipjt.multi_pjt.community.domain.comments;

import java.util.Date;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private int         comments_id;
    private String      comments_contents;
    private Date        comments_date;
    
    // 외래키 
    private int         member_id;
    private int         post_id;

}
