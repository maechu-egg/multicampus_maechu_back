package com.multipjt.multi_pjt.badge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequsetDTO;

@Mapper
public interface UserActivityRecordMapper {
    
    // 새로운 활동 기록 추가
    void insertActivity(UserActivityRecordRequsetDTO record);

    // 같은 날 같은 활동이 있는지 확인
    @Select("SELECT COUNT(*) FROM UserActivityRecord WHERE member_id = #{memberId} AND activity_type = #{activityType} AND DATE(created_date) = CURDATE()")
    int countTodayActivity(@Param("memberId") int memberId, @Param("activityType") String activityType);
}

